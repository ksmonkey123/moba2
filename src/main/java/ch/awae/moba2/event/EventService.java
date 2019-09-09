package ch.awae.moba2.event;

import ch.awae.moba2.config.ProxyConfiguration;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Log
@Service
class EventService {

    private final List<Consumer<Event>> consumers = Collections.synchronizedList(new ArrayList<>());
    private Flux<ServerSentEvent<Event>> eventStream;
    private long lastSetup = 0;

    private final ProxyConfiguration proxyConfiguration;

    public EventService(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    @PostConstruct
    private synchronized void init() {
        backoff();

        if (eventStream != null) {
            throw new IllegalStateException("can not initialize while a stream is present");
        }

        log.info("initialising new SSE event stream");
        WebClient client = WebClient.create(proxyConfiguration.getHost());

        ParameterizedTypeReference<ServerSentEvent<Event>> type =
                new ParameterizedTypeReference<ServerSentEvent<Event>>() {
                };

        Flux<ServerSentEvent<Event>> stream = client.get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(type);

        stream.subscribe(event -> onEvent(event.data()), e -> recreate(stream), () -> recreate(stream));
        this.eventStream = stream;
    }

    private synchronized void recreate(Flux<ServerSentEvent<Event>> stream) {
        if (eventStream == stream) {
            eventStream = null;
            init();
        }
    }

    private void backoff() {
        long now = System.currentTimeMillis();
        long delay = (long) 1000 - (now - lastSetup);
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastSetup = System.currentTimeMillis();
    }

    private void onEvent(Event event) {
        synchronized (consumers) {
            for (Consumer<Event> consumer : consumers) {
                consumer.accept(event);
            }
        }
    }

    public void register(Consumer<Event> consumer) {
        consumers.add(consumer);
    }

}

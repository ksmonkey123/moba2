package ch.awae.moba2.event;

import ch.awae.moba2.LogHelper;
import ch.awae.moba2.buttons.ButtonRegistry;
import ch.awae.moba2.command.CommandClient;
import ch.awae.moba2.config.ProxyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
class EventService {

    private final static Logger LOG = LogHelper.getLogger();

    private final ButtonRegistry buttonRegistry;
    private final ProxyConfiguration proxyConfiguration;
    private final CommandClient commandClient;

    private Flux<ServerSentEvent<Event>> eventStream;
    private long lastSetup = 0;

    @Autowired
    public EventService(ProxyConfiguration proxyConfiguration,
                        ButtonRegistry buttonRegistry,
                        CommandClient commandClient) {
        this.proxyConfiguration = proxyConfiguration;
        this.buttonRegistry = buttonRegistry;
        this.commandClient = commandClient;
    }

    @PostConstruct
    private synchronized void init() {
        backoff();

        if (eventStream != null) {
            throw new IllegalStateException("can not initialize while a stream is present");
        }

        LOG.info("initialising new SSE event stream");
        WebClient client = WebClient.create(proxyConfiguration.getHost());

        ParameterizedTypeReference<ServerSentEvent<Event>> type =
                new ParameterizedTypeReference<ServerSentEvent<Event>>() {
                };

        Flux<ServerSentEvent<Event>> stream = client.get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(type);

        stream.subscribe(event -> onEvent(event.data()), e -> recreate(stream), () -> recreate(stream));
        commandClient.preheat();
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
        if (event != null) {
            LOG.fine("received event: " + event);
            buttonRegistry.setButtons(event.sector, event.input);
        } else {
            LOG.warning("received a null event!");
        }
    }

}

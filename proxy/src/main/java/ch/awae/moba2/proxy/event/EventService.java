package ch.awae.moba2.proxy.event;

import ch.awae.moba2.proxy.DaemonThread;
import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.proxy.Sector;
import ch.awae.moba2.proxy.model.ModelProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class EventService {

    private final Logger LOG = LogHelper.getLogger();

    private final AtomicLong eventCounter = new AtomicLong(0L);
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

    private final Event[] lastEvents;

    private final Object LOCK = new Object();
    private final Thread thread;
    private final ApplicationContext context;
    private final EmitterRepository emitters;

    public EventService(ApplicationContext context, EmitterRepository emitters) {
        this.context = context;
        this.emitters = emitters;
        thread = new DaemonThread("eventSender", this::run);
        lastEvents = new Event[Sector.values().length];
        for (Sector sector : Sector.values()) {
            Event event = new Event(sector, (short) 0);
            event.id = eventCounter.getAndIncrement();
            lastEvents[sector.ordinal()] = event;
        }
    }

    @PostConstruct
    public void setup() {
        thread.start();
    }


    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter();
        LOG.info("created new emitter " + emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        synchronized (LOCK) {
            try {
                sendInitialBurst(emitter);
                emitters.add(emitter);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
        context.getBean(ModelProxy.class).longCommandLight();
        return emitter;
    }

    private void sendInitialBurst(SseEmitter emitter) throws IOException {
        LOG.fine("sending initial event burst to emitter " + emitter);
        for (Event event : lastEvents) {
            emitter.send(event);
        }
    }

    public void sendEvent(Event event) {
        eventQueue.add(event);
    }

    private void run() {
        while (!Thread.interrupted()) {
            try {
                Event event = eventQueue.take();
                synchronized (LOCK) {
                    lastEvents[event.sector.ordinal()] = event;
                    doSend(event);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void doSend(Event event) {
        LOG.fine("sending event " + event);
        event.id = eventCounter.getAndIncrement();
        synchronized (emitters) {
            List<SseEmitter> broken = null;
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(event);
                } catch (IOException | IllegalStateException e) {
                    if (broken == null) {
                        broken = new ArrayList<>();
                    }
                    broken.add(emitter);
                }
            }
            if (broken != null) {
                emitters.removeAll(broken);
            }
        }
    }

    public HealthInfo getHealthInfo() {
        HealthInfo info = new HealthInfo();
        info.eventCount = eventCounter.get();
        info.connections.active = emitters.size();
        info.connections.total = emitters.getLifetimeConnectionCount();
        return info;
    }

    public static class HealthInfo {
        public final ConnectionInfo connections = new ConnectionInfo();
        public long eventCount;

        @Override
        public String toString() {
            return "HealthInfo{" +
                    "connections=" + connections +
                    ", eventCount=" + eventCount +
                    '}';
        }

        static class ConnectionInfo {
            public int active;
            public int total;

            @Override
            public String toString() {
                return "ConnectionInfo{" +
                        "active=" + active +
                        ", total=" + total +
                        '}';
            }
        }

    }
}

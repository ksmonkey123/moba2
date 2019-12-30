package ch.awae.mobaproxy.event;

import ch.awae.mobaproxy.LogHelper;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Repository
public class EmitterRepository implements Iterable<SseEmitter> {

    private final static Logger LOG = LogHelper.getLogger();

    private final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger emitterCount = new AtomicInteger(0);
    private final AtomicInteger totalCount = new AtomicInteger(0);

    public int size() {
        return emitterCount.get();
    }

    void add(SseEmitter emitter) {
        emitters.add(emitter);
        emitterCount.incrementAndGet();
        totalCount.incrementAndGet();
    }

    void remove(SseEmitter emitter) {
        if (emitters.remove(emitter)) {
            LOG.info("removed emitter " + emitter);
            emitterCount.decrementAndGet();
        }
    }

    void removeAll(List<SseEmitter> broken) {
        broken.forEach(this::remove);
    }

    @Override
    public Iterator<SseEmitter> iterator() {
        return emitters.iterator();
    }

    int getLifetimeConnectionCount() {
        return totalCount.get();
    }
}

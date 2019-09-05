package ch.awae.moba2.event;

import java.util.function.Consumer;

public interface EventService {

    void register(Consumer<Event> consumer);

}

package ch.awae.moba2.proxy.event;

import ch.awae.moba2.proxy.Sector;

public class Event {

    public long id = 0;
    public final long timestamp = System.currentTimeMillis();
    public final Sector sector;
    public final int input;

    public Event(Sector sector, int input) {
        this.sector = sector;
        this.input = input;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", sector=" + sector +
                ", input=" + input +
                '}';
    }
}

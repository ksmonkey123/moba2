package ch.awae.moba2.event;

import ch.awae.moba2.Sector;

class Event {
    public long id;
    public long timestamp;
    public Sector sector;
    public int input;

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

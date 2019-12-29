package ch.awae.moba2.event;

import ch.awae.moba2.Sector;

class Event {
    private long id;
    private long timestamp;
    private Sector sector;
    private int input;

    Sector getSector() {
        return sector;
    }

    int getInput() {
        return input;
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

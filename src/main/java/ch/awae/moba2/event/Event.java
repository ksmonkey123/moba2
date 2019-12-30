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

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setInput(int input) {
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

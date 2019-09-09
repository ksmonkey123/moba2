package ch.awae.moba2.event;

import ch.awae.moba2.Sector;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
class Event {
    private long id;
    private long timestamp;
    private Sector sector;
    private int input;
}

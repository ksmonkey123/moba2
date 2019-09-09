package ch.awae.moba2.event;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Event {
    private long id;
    private long timestamp;
    private String sector;
    private int input;
}

package ch.awae.moba2.path;

import ch.awae.moba2.Sector;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Path {

    private final String title;
    private final Sector sector;
    private final int mask;
    private final int data;

    public boolean collides(Path other) {
        return (this.sector == other.sector) && ((this.mask & other.mask) != 0);
    }
}

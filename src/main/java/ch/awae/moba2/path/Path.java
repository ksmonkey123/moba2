package ch.awae.moba2.path;

import ch.awae.moba2.Sector;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class Path {

    private final String title;
    private final Sector sector;
    private final int mask;
    private final int data;

    boolean collides(Path other) {
        return (this.sector == other.sector) && ((this.mask & other.mask) != 0);
    }

    public boolean isClear() {
        return data == 0;
    }

}

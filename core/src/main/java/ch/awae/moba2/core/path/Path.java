package ch.awae.moba2.core.path;

import ch.awae.moba2.core.Sector;

import java.util.Objects;

public final class Path {

    private final String title;
    private final Sector sector;
    private final int mask;
    private final int data;

    Path(String title, Sector sector, int mask, int data) {
        this.title = title;
        this.sector = sector;
        this.mask = mask;
        this.data = data;
    }

    boolean collides(Path other) {
        return (this.sector == other.sector) && ((this.mask & other.mask) != 0);
    }

    public boolean isClear() {
        return data == 0;
    }

    public String getTitle() {
        return title;
    }

    public Sector getSector() {
        return sector;
    }

    int getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return mask == path.mask &&
                data == path.data &&
                Objects.equals(title, path.title) &&
                sector == path.sector;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, sector, mask, data);
    }

    @Override
    public String toString() {
        return sector.name() + "/" + title;
    }

}

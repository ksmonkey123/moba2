package ch.awae.moba2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sector {
    BOTTOM(0, true),
    LEFT(1, true),
    RIGHT(2, true),
    CENTER(3, true),
    LIGHTS(4, false);

    public final int id;
    public final boolean switchSector;

    public static Sector getById(int id) {
        for (Sector sector : Sector.values()) {
            if (sector.id == id) {
                return sector;
            }
        }
        return null;
    }
}

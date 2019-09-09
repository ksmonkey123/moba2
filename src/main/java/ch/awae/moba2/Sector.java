package ch.awae.moba2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sector {
    BOTTOM(0),
    LEFT(1),
    RIGHT(2),
    CENTER(3),
    LIGHTS(4);

    public final int id;

    public static Sector getById(int id) {
        for (Sector sector : Sector.values()) {
            if (sector.id == id) {
                return sector;
            }
        }
        return null;
    }
}

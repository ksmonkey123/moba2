package ch.awae.moba2.buttons;

import ch.awae.moba2.Sector;
import org.springframework.stereotype.Service;

@Service
public class ButtonRegistry {

    private volatile int[] sectors = new int[Sector.values().length];

    public boolean isPressed(Sector sector, int mask) {
        return (sectors[sector.ordinal()] & mask) > 0;
    }

    public void setButtons(Sector sector, int input) {
        sectors[sector.ordinal()] = input;
    }
}

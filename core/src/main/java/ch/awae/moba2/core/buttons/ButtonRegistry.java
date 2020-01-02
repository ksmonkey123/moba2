package ch.awae.moba2.core.buttons;

import ch.awae.moba2.core.Sector;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Component
public class ButtonRegistry {

    private volatile int[] sectors = new int[Sector.values().length];

    boolean isPressed(@NotNull Sector sector, int mask) {
        return (sectors[sector.ordinal()] & mask) > 0;
    }

    public void setButtons(@NotNull Sector sector, int input) {
        sectors[sector.ordinal()] = input;
    }
}

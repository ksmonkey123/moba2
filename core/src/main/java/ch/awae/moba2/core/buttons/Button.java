package ch.awae.moba2.core.buttons;

import ch.awae.moba2.core.Sector;
import ch.awae.utils.logic.Logic;

final class Button implements Logic {

    private final ButtonRegistry registry;
    private final Sector sector;
    private final int mask;

    Button(ButtonRegistry registry, Sector sector, int mask) {
        this.registry = registry;
        this.sector = sector;
        this.mask = mask;
    }

    @Override
    public boolean evaluate() {
        return registry.isPressed(sector, mask);
    }

}

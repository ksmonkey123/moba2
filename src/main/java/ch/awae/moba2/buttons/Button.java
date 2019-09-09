package ch.awae.moba2.buttons;

import ch.awae.moba2.Sector;
import ch.awae.utils.logic.Logic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class Button implements Logic {

    private final ButtonRegistry registry;
    private final Sector sector;
    private final int mask;

    @Override
    public boolean evaluate() {
        return registry.isPressed(sector, mask);
    }

}

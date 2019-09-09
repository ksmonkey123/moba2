package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Controller;
import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import org.springframework.stereotype.Component;

@Component
public class LeftClearController implements Controller {

    private final PathRegistry pathRegistry;

    private final Logic clear;
    private final Path p_clear;

    public LeftClearController(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        clear = buttonProvider.getButton(Sector.LEFT, "clear");
        p_clear = pathProvider.getPath("left.clear");
    }

    @Override
    public void tick() {
        clear.test(() -> pathRegistry.register(p_clear));
    }
}

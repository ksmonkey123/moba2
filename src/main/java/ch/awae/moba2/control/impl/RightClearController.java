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
public class RightClearController implements Controller {

    private final PathRegistry pathRegistry;

    private final Logic clear_a, clear_b;
    private final Path p_clear_a, p_clear_b;

    public RightClearController(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        SectorButtonProvider provider = buttonProvider.sector(Sector.RIGHT);
        this.clear_a = provider.button("clear.A");
        this.clear_b = provider.button("clear.B");

        this.p_clear_a = pathProvider.getPath("right.clear_A");
        this.p_clear_b = pathProvider.getPath("right.clear_B");
    }

    @Override
    public void tick() {
        clear_a.test(() -> pathRegistry.register(p_clear_a));
        clear_b.test(() -> pathRegistry.register(p_clear_b));
    }
}

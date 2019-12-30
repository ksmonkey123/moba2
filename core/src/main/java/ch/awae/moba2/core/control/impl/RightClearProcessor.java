package ch.awae.moba2.core.control.impl;

import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.buttons.ButtonProvider;
import ch.awae.moba2.core.buttons.SectorButtonProvider;
import ch.awae.moba2.core.control.Processor;
import ch.awae.moba2.core.path.Path;
import ch.awae.moba2.core.path.PathProvider;
import ch.awae.moba2.core.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RightClearProcessor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic clear_a, clear_b;
    private final Path p_clear_a, p_clear_b;

    @Autowired
    public RightClearProcessor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
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

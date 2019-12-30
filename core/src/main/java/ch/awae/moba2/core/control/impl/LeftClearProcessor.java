package ch.awae.moba2.core.control.impl;

import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.buttons.ButtonProvider;
import ch.awae.moba2.core.control.Processor;
import ch.awae.moba2.core.path.Path;
import ch.awae.moba2.core.path.PathProvider;
import ch.awae.moba2.core.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeftClearProcessor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic clear;
    private final Path p_clear;

    @Autowired
    public LeftClearProcessor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;
        clear = buttonProvider.getButton(Sector.LEFT, "clear");
        p_clear = pathProvider.getPath("left.clear");
    }

    @Override
    public void tick() {
        clear.test(() -> pathRegistry.register(p_clear));
    }
}

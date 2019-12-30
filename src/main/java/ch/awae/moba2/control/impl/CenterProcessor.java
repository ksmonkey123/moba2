package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Processor;
import ch.awae.moba2.path.Path;
import ch.awae.moba2.path.PathProvider;
import ch.awae.moba2.path.PathRegistry;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CenterProcessor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic[] buttons;
    private final Logic clear;
    private final Path p_clear;
    private final Path[] paths;

    @Autowired
    public CenterProcessor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;

        SectorButtonProvider provider = buttonProvider.sector(Sector.CENTER);

        buttons = provider.group("buttons").toArray();
        clear = provider.button("clear");

        p_clear = pathProvider.getPath("center.clear");
        paths = pathProvider.getPaths("center.1a", "center.1b", "center.2a",
                "center.2b", "center.3a", "center.3b", "center.4a", "center.4b");
    }

    @Override
    public void tick() {
        if (clear.evaluate()) {
            pathRegistry.register(p_clear);
            return;
        }

        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].evaluate()) {
                pathRegistry.register(paths[i]);
            }
        }
    }
}

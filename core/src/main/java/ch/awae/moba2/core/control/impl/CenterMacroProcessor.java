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
public class CenterMacroProcessor implements Processor {

    private final PathRegistry pathRegistry;

    private final Logic logic_1, logic_2;
    private final Path  path_1b, path_2b, path_3a, path_3b;

    @Autowired
    public CenterMacroProcessor(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
        this.pathRegistry = pathRegistry;

        SectorButtonProvider provider = buttonProvider.sector(Sector.CENTER);

        Logic S0 = provider.group("switch[0].group").any();
        Logic S1 = provider.group("switch[1].group").any();
        Logic S3 = provider.group("switch[3].group").any();

        this.logic_1 = S0.and(S3);
        this.logic_2 = S1.and(S3);

        this.path_1b = pathProvider.getPath("center.1b");
        this.path_2b = pathProvider.getPath("center.2b");
        this.path_3a = pathProvider.getPath("center.3a");
        this.path_3b = pathProvider.getPath("center.3b");
    }

    @Override
    public void tick() {
        if (logic_1.evaluate()) {
            pathRegistry.register(path_2b, path_3a);
        }
        if (logic_2.evaluate()) {
            pathRegistry.register(path_1b, path_3b);
        }
    }
}

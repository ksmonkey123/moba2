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
public class CenterMacroController implements Controller {

    private final PathRegistry pathRegistry;

    private final Logic logic_1, logic_2;
    private final Path  path_1b, path_2b, path_3a, path_3b;

    public CenterMacroController(ButtonProvider buttonProvider, PathProvider pathProvider, PathRegistry pathRegistry) {
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

package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.buttons.SectorButtonProvider;
import ch.awae.moba2.control.Controller;
import ch.awae.moba2.lights.Light;
import ch.awae.moba2.lights.LightProvider;
import ch.awae.utils.logic.Logic;
import org.springframework.stereotype.Component;

@Component
public class LightController implements Controller {

    private Light all, roads;
    private Logic allOff, allOn;

    public LightController(ButtonProvider buttonProvider, LightProvider provider) {
        SectorButtonProvider buttons = buttonProvider.sector(Sector.LIGHTS);

        all = provider.getLight("all");
        roads = provider.getLight("roads");

        allOff = buttons.button("light[1]").edge();
        allOn = buttons.button("light[2]").edge();
    }

    @Override
    public void tick() {
        allOff.test(all::disable);
        allOn.test(all::enable);
    }
}

package ch.awae.moba2.core.control.impl;

import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.buttons.ButtonProvider;
import ch.awae.moba2.core.buttons.SectorButtonProvider;
import ch.awae.moba2.core.control.Processor;
import ch.awae.moba2.core.lights.Light;
import ch.awae.moba2.core.lights.LightProvider;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LightProcessor implements Processor {

    private Light all;
    private Logic allOff, allOn;

    @Autowired
    public LightProcessor(ButtonProvider buttonProvider, LightProvider provider) {
        SectorButtonProvider buttons = buttonProvider.sector(Sector.LIGHTS);

        all = provider.getLight("all");

        allOff = buttons.button("light[1]").edge();
        allOn = buttons.button("light[2]").edge();
    }

    @Override
    public void tick() {
        allOff.test(all::disable);
        allOn.test(all::enable);
    }
}

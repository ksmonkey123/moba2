package ch.awae.moba2.control.impl;

import ch.awae.moba2.Sector;
import ch.awae.moba2.buttons.ButtonProvider;
import ch.awae.moba2.control.Processor;
import ch.awae.moba2.lights.Light;
import ch.awae.moba2.lights.LightProvider;
import ch.awae.utils.logic.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StepThroughLightProcessor implements Processor {

    private final Logic start, previous, next;
    private final LightProvider lightProvider;

    private int id = 0;
    private Light current = null;

    @Autowired
    public StepThroughLightProcessor(LightProvider lightProvider, ButtonProvider buttonProvider) {
        this.lightProvider = lightProvider;

        this.start = buttonProvider.getButton(Sector.LIGHTS, "light[3]").edge();
        this.previous = buttonProvider.getButton(Sector.LIGHTS, "light[4]").edge();
        this.next = buttonProvider.getButton(Sector.LIGHTS, "light[5]").edge();
    }

    @Override
    public void tick() {
        start.test(this::start);
        previous.test(this::back);
        next.test(this::next);
    }

    private void start() {
        id = 0;
        current = lightProvider.getLight(0, 0);
        current.enable();
    }

    private void back() {
        current.disable();
        id = (id + 127) % 128;

        current = lightProvider.getLight(id / 8, id % 8);
        current.enable();
    }

    private void next() {
        current.disable();
        id = (id + 1) % 128;

        current = lightProvider.getLight(id / 8, id % 8);
        current.enable();
    }
}

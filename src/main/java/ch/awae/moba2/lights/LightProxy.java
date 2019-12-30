package ch.awae.moba2.lights;

import ch.awae.moba2.LogHelper;

import java.util.logging.Logger;

class LightProxy implements Light {

    private final static Logger logger = LogHelper.getLogger();

    private final String id;
    private final Light light;

    LightProxy(String id, Light light) {
        this.id = id;
        this.light = light;
    }


    @Override
    public void enable() {
        logger.info("enabling light " + id);
        light.enable();
    }

    @Override
    public void disable() {
        logger.info("disabling light " + id);
        light.disable();
    }

    @Override
    public void toggle() {
        logger.info("toggling light " + id);
        light.toggle();
    }

    @Override
    public boolean isEnabled() {
        return light.isEnabled();
    }

    @Override
    public boolean isDisabled() {
        return light.isDisabled();
    }
}

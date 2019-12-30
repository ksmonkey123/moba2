package ch.awae.moba2.core.lights;

import ch.awae.moba2.common.LogHelper;

import java.util.logging.Logger;

class LightProxy implements Light {

    private final static Logger LOG = LogHelper.getLogger();

    private final String id;
    private final Light light;

    LightProxy(String id, Light light) {
        this.id = id;
        this.light = light;
    }


    @Override
    public void enable() {
        LOG.info("enabling light " + id);
        light.enable();
    }

    @Override
    public void disable() {
        LOG.info("disabling light " + id);
        light.disable();
    }

    @Override
    public void toggle() {
        LOG.info("toggling light " + id);
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

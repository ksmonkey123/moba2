package ch.awae.moba2.lights;

class LightGroup implements Light {

    private final Light[] lights;

    LightGroup(Light[] lights) {
        this.lights = lights;
    }

    @Override
    public void enable() {
        for (Light light : lights) {
            light.enable();
        }
    }

    @Override
    public void disable() {
        for (Light light : lights) {
            light.disable();
        }
    }

    @Override
    public void toggle() {
        for (Light light : lights) {
            light.toggle();
        }
    }

    @Override
    public boolean isEnabled() {
        for (Light light : lights) {
            if (!light.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDisabled() {
        for (Light light : lights) {
            if (!light.isDisabled()) {
                return false;
            }
        }
        return true;
    }
}

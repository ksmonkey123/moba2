package ch.awae.moba2.core.lights;

class LightImpl implements Light {

    private final int chip, pin;
    private final LightModel model;

    LightImpl(int chip, int pin, LightModel model) {
        this.chip = chip;
        this.pin = pin;
        this.model = model;
    }

    @Override
    public void enable() {
        model.setPin(chip, pin, true);
    }

    @Override
    public void disable() {
        model.setPin(chip, pin, false);
    }

    @Override
    public void toggle() {
        model.setPin(chip, pin, isDisabled());
    }

    @Override
    public boolean isEnabled() {
        return model.getPin(chip, pin);
    }

    @Override
    public boolean isDisabled() {
        return !model.getPin(chip, pin);
    }
}

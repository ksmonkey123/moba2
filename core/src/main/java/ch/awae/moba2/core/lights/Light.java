package ch.awae.moba2.core.lights;

public interface Light {
    void enable();
    void disable();
    void toggle();
    boolean isEnabled();
    boolean isDisabled();
}

package ch.awae.moba2.command;

import java.util.Arrays;

public class LightCommand {

    private final String[] enabled;
    private final boolean test;

    public LightCommand(String[] enabled) {
        this(enabled, false);
    }

    LightCommand(String[] enabled, boolean test) {
        this.enabled = enabled;
        this.test = test;
    }

    public String[] getEnabled() {
        return enabled;
    }

    public boolean isTest() {
        return test;
    }

    @Override
    public String toString() {
        return "LightCommand{" +
                "enabled=" + Arrays.toString(enabled) +
                ", test=" + test +
                '}';
    }
}

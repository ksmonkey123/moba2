package ch.awae.mobaproxy.lights;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class LightCommand {

    public @NotNull String[] enabled;
    public boolean test;

    public String prettyPrint() {
        return  toString() + '{' +
                "test=" + test +
                ", enabled=" + Arrays.toString(enabled) +
                '}';
    }

    @Override
    public String toString() {
        // shortened standard toString
        return "LightCommand@" + Integer.toHexString(hashCode());
    }
}

package ch.awae.mobaproxy.switches;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class SwitchCommand {
    public @NotNull @Range(max = 255) Integer command;
    public @NotNull @Range(max = 65535) Integer display;
    public boolean test;

    @Override
    public String toString() {
        return "SwitchCommand@"+ Integer.toHexString(hashCode()) +'{' +
                "command=" + command +
                ", display=" + display +
                ", test=" + test +
                '}';
    }

}

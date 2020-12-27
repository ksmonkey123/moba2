package ch.awae.moba2.core.lights;

import ch.awae.moba2.core.command.CommandClient;
import ch.awae.moba2.core.command.LightCommand;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

//@Validated
@Component
public class LightModel {

    private volatile boolean[][] pins = new boolean[16][8];
    private volatile boolean changed = false;

    private final CommandClient client;

    @Autowired
    public LightModel(CommandClient client) {
        this.client = client;
    }

    public void setPin(
            @Range(min = 0, max = 15) int chip,
            @Range(min = 0, max = 7) int pin,
            boolean enabled) {
        boolean previous = pins[chip][pin];
        if (previous != enabled) {
            pins[chip][pin] = enabled;
            changed = true;
        }
    }

    public boolean getPin(
            @Range(min = 0, max = 15) int chip,
            @Range(min = 0, max = 7) int pin) {
        return pins[chip][pin];
    }

    public void flushChanges() {
        if (!changed) {
            return;
        }

        List<String> list = new ArrayList<>();
        for (int chip = 0; chip < pins.length; chip++) {
            for (int pin = 0; pin < pins[chip].length; pin++) {
                if (pins[chip][pin]) {
                    list.add(chip + ":" + pin);
                }
            }
        }

        client.sendLightCommand(new LightCommand(list.toArray(new String[]{})));
        changed = false;
    }

}

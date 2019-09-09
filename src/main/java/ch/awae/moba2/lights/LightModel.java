package ch.awae.moba2.lights;

import ch.awae.moba2.command.CommandClient;
import ch.awae.moba2.command.LightCommand;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LightModel {

    private volatile boolean[][] pins = new boolean[16][8];
    private volatile boolean changed = false;

    private final CommandClient client;

    public LightModel(CommandClient client) {
        this.client = client;
    }

    public void setPin(int chip, int pin, boolean enabled) {
        boolean previous = pins[chip][pin];
        if (previous != enabled) {
            pins[chip][pin] = enabled;
            changed = true;
        }
    }

    public boolean getPin(int chip, int pin) {
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

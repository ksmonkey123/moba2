package ch.awae.moba2.proxy.lights;

import ch.awae.moba2.proxy.model.Model;
import ch.awae.moba2.proxy.spi.SpiDataBundle;

import java.util.Arrays;
import java.util.List;

public class LightModel implements Model<LightCommand> {

    private boolean[] pins = new boolean[128];
    private boolean[] flag = new boolean[128];

    private static final int RUNS = 2;
    private int run = 1;
    private int decoder = 7;
    private int block = 7;
    private byte cache = 0;

    @Override
    public SpiDataBundle getNextBundle() {
        run = (run + 1) % RUNS;
        if (run == 0) {
            decoder = (decoder + 1) % 8;
            if (decoder == 0) {
                block = (block + 1) % 8;
            }
            cache = getOptimal(decoder, block);
        }

        return new SpiDataBundle(cache, (short) 0);
    }

    @Override
    public void update(LightCommand command) {
        List<String> tokens = Arrays.asList(command.enabled);

        for (int chip = 0; chip < 16; chip++) {
            for (int pin = 0; pin < 8; pin++) {
                if (tokens.contains(chip + ":" + pin)
                        || tokens.contains(chip + ":*")
                        || tokens.contains("*:*")) {
                    setPin(chip, pin, true);
                } else {
                    setPin(chip, pin, false);
                }
            }
        }
    }

    private void setPin(int chip, int pin, boolean state) {
        int id = 8 * chip + pin;
        if (pins[id] != state) {
            pins[id] = state;
            flag[id] = true;
        }
    }

    private byte getOptimal(int decoder, int dflt) {
        for (int i = 0; i < 8; i++) {
            int baseID = 2 * (8 * decoder + i);
            if (flag[baseID] || flag[baseID + 1]) {
                // clear touch flag
                flag[baseID] = false;
                flag[baseID + 1] = false;
                // get data
                return getData(decoder, i);
            }
        }
        // no priority data. take default series
        return getData(decoder, dflt);
    }

    private byte getData(int decoder, int pair) {
        byte result = (byte) (((decoder << 5) & 0x000000e0) | ((pair << 2) & 0x0000001c) & 0xff);
        result += pins[2 * (8 * decoder + pair)] ? 1 : 0;
        result += pins[2 * (8 * decoder + pair) + 1] ? 2 : 0;
        return result;
    }

    @Override
    public long getLastUpdate() {
        return 0;
    }
}

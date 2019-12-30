package ch.awae.moba2.proxy.switches;

import ch.awae.moba2.proxy.model.Model;
import ch.awae.moba2.proxy.spi.SpiDataBundle;

public class SwitchModel implements Model<SwitchCommand> {

    private byte command = 0;
    private short display = 0;
    private long updated = 0L;

    @Override
    public SpiDataBundle getNextBundle() {
        return new SpiDataBundle(command, display);
    }

    @Override
    public void update(SwitchCommand command) {
        if (this.command != command.command || this.display != command.display) {
            this.updated = System.currentTimeMillis();
        }
        this.command = (byte) (0x000000ff & command.command);
        this.display = (short) (0x0000ffff & command.display);
    }

    @Override
    public long getLastUpdate() {
        return updated;
    }
}

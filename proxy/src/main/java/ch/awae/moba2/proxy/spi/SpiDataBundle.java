package ch.awae.moba2.proxy.spi;

public class SpiDataBundle {

    public final byte command;
    public short display;

    public SpiDataBundle(byte command, short display) {
        this.command = command;
        this.display = display;
    }

}

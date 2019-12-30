package ch.awae.moba2.proxy;

import ch.awae.moba2.proxy.spi.SpiChannel;

public enum Sector {

    BOTTOM(SpiChannel.CH_0, 0x00008fff),
    LEFT(SpiChannel.CH_1, 0x000001fe),
    RIGHT(SpiChannel.CH_2, 0x000000ff),
    CENTER(SpiChannel.CH_3, 0x000001ff),
    LIGHTS(SpiChannel.CH_4, 0x0000007f);

    public final SpiChannel channel;
    public final int bitmask;

    Sector(SpiChannel channel, int bitmask) {
        this.channel = channel;
        this.bitmask = bitmask;
    }
}

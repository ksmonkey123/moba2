package ch.awae.moba2.proxy.spi;

public interface SpiService {

    int update(SpiChannel channel, byte command, short display);

}

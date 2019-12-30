package ch.awae.mobaproxy.spi;

public interface SpiService {

    int update(SpiChannel channel, byte command, short display);

}

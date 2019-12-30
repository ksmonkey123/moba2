package ch.awae.mobaproxy.model;

import ch.awae.mobaproxy.spi.SpiDataBundle;

public interface Model<T> {

    SpiDataBundle getNextBundle();

    void update(T command);

    long getLastUpdate();
}

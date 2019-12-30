package ch.awae.moba2.proxy.model;

import ch.awae.moba2.proxy.spi.SpiDataBundle;

public interface Model<T> {

    SpiDataBundle getNextBundle();

    void update(T command);

    long getLastUpdate();
}

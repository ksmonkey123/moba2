package ch.awae.moba2.proxy.spi;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum SpiChannel {

    CH_0(RaspiPin.GPIO_00),
    CH_1(RaspiPin.GPIO_01),
    CH_2(RaspiPin.GPIO_02),
    CH_3(RaspiPin.GPIO_03),
    CH_4(RaspiPin.GPIO_04);

    public final Pin pin;

    SpiChannel(Pin pin) {
        this.pin = pin;
    }
}

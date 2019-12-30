package ch.awae.moba2.proxy.spi;

import com.pi4j.io.gpio.*;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

@Service
@Profile("!dev")
class SpiServiceImpl implements SpiService {

    private final Map<SpiChannel, GpioPinDigitalOutput> pinMap;
    private final SpiDevice spi;
    private final byte magicNumber;
    private final long hostSelectDelay;

    @Autowired
    public SpiServiceImpl(SpiConfiguration configuration) throws IOException {
        this.pinMap = new HashMap<>();
        this.spi = SpiFactory.getInstance(com.pi4j.io.spi.SpiChannel.CS0, configuration.getSpeed(), SpiMode.MODE_0);

        this.magicNumber = configuration.getMagicNumber();
        this.hostSelectDelay = configuration.getHostSelectDelay();

        GpioController gpioController = GpioFactory.getInstance();
        for (SpiChannel channel : SpiChannel.values()) {
            GpioPinDigitalOutput pin = gpioController.provisionDigitalOutputPin(channel.pin, PinState.LOW);
            pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, PinMode.DIGITAL_INPUT);
            this.pinMap.put(channel, pin);
        }
    }

    @Override
    public int update(SpiChannel channel, byte command, short display) {
        try {
            GpioPinDigitalOutput pin = pinMap.get(channel);
            byte[] data = {magicNumber, (byte) (display & 0x00ff),
                    (byte) ((display >> 8) & 0x00ff), command};

            byte[] response = runSPI(pin, data);

            if (response[0] != magicNumber) {
                return -1;
            }
            byte check = (byte) ((data[1] ^ data[2]) & 0x000000ff);
            if (response[3] != check) {
                // invalid read-back
                return -1;
            }
            return ((response[2] << 8) & 0x0000ff00) | (response[1] & 0x000000ff);
        } catch (IOException e) {
            return -1;
        }
    }

    private byte[] runSPI(GpioPinDigitalOutput pin, byte[] data) throws IOException {
        pin.setState(PinState.HIGH);
        try {
            if (hostSelectDelay > 0) {
                LockSupport.parkNanos(hostSelectDelay);
            }
            return spi.write(data);
        } finally {
            pin.setState(PinState.LOW);
        }
    }
}

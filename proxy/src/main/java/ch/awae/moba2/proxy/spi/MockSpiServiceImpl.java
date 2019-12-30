package ch.awae.moba2.proxy.spi;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class MockSpiServiceImpl implements SpiService {
    @Override
    public int update(SpiChannel channel, byte command, short display) {
        return -1;
    }
}

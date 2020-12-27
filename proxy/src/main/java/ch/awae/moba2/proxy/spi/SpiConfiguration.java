package ch.awae.moba2.proxy.spi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

//@Validated
@Configuration
@ConfigurationProperties("spi")
public class SpiConfiguration {

    private @NotNull Integer speed;
    private @NotNull Long hostSelectDelay;
    private @NotNull Byte magicNumber;
    private @NotNull @PositiveOrZero Integer maxRestore;

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Long getHostSelectDelay() {
        return hostSelectDelay;
    }

    public void setHostSelectDelay(Long hostSelectDelay) {
        this.hostSelectDelay = hostSelectDelay;
    }

    public Byte getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(Byte magicNumber) {
        this.magicNumber = magicNumber;
    }

    public Integer getMaxRestore() {
        return maxRestore;
    }

    public void setMaxRestore(Integer maxRestore) {
        this.maxRestore = maxRestore;
    }
}

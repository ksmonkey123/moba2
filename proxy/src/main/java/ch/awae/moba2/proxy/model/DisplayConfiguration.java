package ch.awae.moba2.proxy.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

//@Validated
@Configuration
@ConfigurationProperties("display")
public class DisplayConfiguration {

    private @NotNull @PositiveOrZero Integer stealthModeFadeOut;
    private @NotNull @PositiveOrZero Integer commandFlashDuration;

    public Integer getStealthModeFadeOut() {
        return stealthModeFadeOut;
    }

    public void setStealthModeFadeOut(Integer stealthModeFadeOut) {
        this.stealthModeFadeOut = stealthModeFadeOut;
    }

    public Integer getCommandFlashDuration() {
        return commandFlashDuration;
    }

    public void setCommandFlashDuration(Integer commandFlashDuration) {
        this.commandFlashDuration = commandFlashDuration;
    }
}

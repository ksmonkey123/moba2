package ch.awae.moba2.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Validated
@Configuration
@ConfigurationProperties("provider")
public class ProviderConfiguration {

    private @NotNull @Pattern(regexp = ".*\\.yml") String button;
    private @NotNull @Pattern(regexp = ".*\\.yml") String light;
    private @NotNull @Pattern(regexp = ".*\\.yml") String path;

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

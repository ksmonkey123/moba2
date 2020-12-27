package ch.awae.moba2.core.config;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Validated
@Configuration
@ConfigurationProperties("proxy")
public class ProxyConfiguration {

    private @NotNull @URL @Pattern(regexp = ".*/") String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}

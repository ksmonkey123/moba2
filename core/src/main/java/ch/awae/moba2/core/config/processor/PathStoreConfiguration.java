package ch.awae.moba2.core.config.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Validated
@Configuration
@ConfigurationProperties("processor.path-store")
public class PathStoreConfiguration {

    private @Positive long saveTimeout;

    public long getSaveTimeout() {
        return saveTimeout;
    }

    public void setSaveTimeout(long saveTimeout) {
        this.saveTimeout = saveTimeout;
    }

}

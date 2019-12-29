package ch.awae.moba2.config.control;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("control.path-store")
public class PathStoreConfiguration {

    private long saveTimeout;

    public long getSaveTimeout() {
        return saveTimeout;
    }

    public void setSaveTimeout(long saveTimeout) {
        this.saveTimeout = saveTimeout;
    }

}

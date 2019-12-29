package ch.awae.moba2.config.control;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties("control")
public class ControlConfiguration {

    private PathStoreConfiguration pathStore;

    @Bean
    public PathStoreConfiguration getPathStore() {
        return pathStore;
    }
}

package ch.awae.moba2.core.config;

import ch.awae.moba2.common.LogHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

@Validated
@Configuration
@ConfigurationProperties("app")
public class AppConfiguration {

    private final static Logger LOG = LogHelper.getLogger();

    private @NotNull String artifactId;
    private @NotNull String version;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @PostConstruct
    public void logInfo() {
        LOG.info("launched " + artifactId + " v" + version);
    }

}

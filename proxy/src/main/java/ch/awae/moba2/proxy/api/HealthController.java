package ch.awae.moba2.proxy.api;

import ch.awae.moba2.proxy.AppProperties;
import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.proxy.event.EventService;
import ch.awae.moba2.proxy.spi.SpiTransmissionController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class HealthController {

    private final static Logger LOG = LogHelper.getLogger();

    private final EventService eventService;
    private final SpiTransmissionController transmissionController;
    private final CommandController commandController;
    private String artifactId;
    private String version;

    private final long startTime = System.currentTimeMillis();

    public HealthController(EventService eventService,
                            SpiTransmissionController transmissionController,
                            CommandController commandController,
                            AppProperties appProperties) {
        this.eventService = eventService;
        this.transmissionController = transmissionController;
        this.commandController = commandController;
        this.artifactId = appProperties.getArtifactId();
        this.version = appProperties.getVersion();
    }

    @GetMapping("/health")
    public HealthInfo getHealthInfo() {
        LOG.info("providing health information");
        HealthInfo info = new HealthInfo();

        info.application = artifactId;
        info.version = version;
        info.uptime = System.currentTimeMillis() - startTime;

        info.events = eventService.getHealthInfo();
        info.spi = transmissionController.getHealthInfo();
        info.commands = commandController.getHealthInfo();

        LOG.fine("sending health data: " + info);

        return info;
    }

}

class HealthInfo {
    public String application;
    public String version;
    public long timestamp = System.currentTimeMillis();
    public long uptime;
    public EventService.HealthInfo events;
    public SpiTransmissionController.HealthInfo spi;
    public CommandController.HealthInfo commands;

    @Override
    public String toString() {
        return "HealthInfo{" +
                "application='" + application + '\'' +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", uptime=" + uptime +
                ", events=" + events +
                ", spi=" + spi +
                ", commands=" + commands +
                '}';
    }
}

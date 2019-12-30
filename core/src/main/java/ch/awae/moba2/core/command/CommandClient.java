package ch.awae.moba2.core.command;

import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.core.Sector;
import ch.awae.moba2.core.config.ProxyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CommandClient {

    private static final Logger LOG = LogHelper.getLogger();

    private final RestTemplate http;
    private final ProxyConfiguration configuration;

    @Autowired
    public CommandClient(RestTemplate http, ProxyConfiguration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public void sendSwitchCommand(Sector sector, SwitchCommand switchCommand) {
        LOG.fine(String.format("sending switch command to sector %s: %s", sector, switchCommand));
        http.postForObject(configuration.getHost() + "switch/" + sector.name(), switchCommand, Object.class);
    }

    public void sendLightCommand(LightCommand lightCommand) {
        LOG.fine(String.format("sending light command: %s", lightCommand));
        http.postForObject(configuration.getHost() + "lights", lightCommand, Object.class);
    }

    public void preheat() {
        LOG.info("preheating proxy");
        try {
            for (int i = 0; i < 4; i++) {
                doPreheat();
            }
            LOG.info("proxy preheated");
        } catch(Exception e) {
            LOG.log(Level.WARNING, "proxy preheating failed: " + e.toString());
            LOG.log(Level.FINE, e.toString(), e);
        }
    }

    private void doPreheat() {
        for (Sector sector : Sector.values()) {
            if (sector.switchSector) {
                sendSwitchCommand(sector, new SwitchCommand(0, 0, true));
            } else {
                sendLightCommand(new LightCommand(new String[0], true));
            }
        }
    }
}

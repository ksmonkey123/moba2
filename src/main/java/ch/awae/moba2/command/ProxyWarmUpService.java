package ch.awae.moba2.command;

import ch.awae.moba2.Sector;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;

@Log
@Service
public class ProxyWarmUpService {

    private final CommandClient client;

    @Autowired
    public ProxyWarmUpService(CommandClient client) {
        this.client = client;
    }

    public void preheat() {
        log.info("preheating proxy");
        try {
            for (int i = 0; i < 4; i++) {
                doPreheat();
            }
            log.info("preheating completed");
        } catch(Exception e) {
            log.log(Level.WARNING, "preheating failed", e);
        }
    }

    private void doPreheat() {
        for (Sector sector : Sector.values()) {
            if (sector.switchSector) {
                client.sendSwitchCommand(sector, new SwitchCommand(0, 0, true));
            } else {
                client.sendLightCommand(new LightCommand(new String[0], true));
            }
        }
    }

}

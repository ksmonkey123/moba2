package ch.awae.mobaproxy.api;

import ch.awae.mobaproxy.LogHelper;
import ch.awae.mobaproxy.Sector;
import ch.awae.mobaproxy.lights.LightCommand;
import ch.awae.mobaproxy.model.ModelProxy;
import ch.awae.mobaproxy.switches.SwitchCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RestController
public class CommandController {

    private final static Logger LOG = LogHelper.getLogger();

    private final ModelProxy modelProxy;
    private final Map<Sector, AtomicLong> counters;

    public CommandController(ModelProxy modelProxy) {
        this.modelProxy = modelProxy;
        this.counters = new ConcurrentHashMap<>();
        for (Sector sector : Sector.values()) {
            counters.put(sector, new AtomicLong(0L));
        }
    }

    @PostMapping("/switch/{sector}")
    public void handleSwitchCommand(@PathVariable("sector") Sector sector, @Valid @RequestBody SwitchCommand command) {
        Objects.requireNonNull(sector);
        LOG.fine("received switch command for sector " + sector + ": " + command);
        if (sector == Sector.LIGHTS) {
            throw new BadRequestException(sector + " is not a switch sector");
        }
        if (!command.test) {
            modelProxy.applySwitchCommand(sector, command);
            counters.get(sector).incrementAndGet();
        }
    }

    @PostMapping("/lights")
    public void handleLights(@Valid @RequestBody LightCommand command) {
        LOG.fine("received light command: " + command.prettyPrint());
        if (!command.test) {
            modelProxy.applyLightCommand(command);
            counters.get(Sector.LIGHTS).incrementAndGet();
        }
    }

    HealthInfo getHealthInfo() {
        return new HealthInfo(counters);
    }

    static class HealthInfo {
        private final Map<Sector, AtomicLong> processed;

        public Map<Sector, AtomicLong> getProcessed() {
            return processed;
        }

        private HealthInfo(Map<Sector, AtomicLong> processed) {
            this.processed = processed;
        }

        @Override
        public String toString() {
            return "HealthInfo{" +
                    "processed=" + processed +
                    '}';
        }
    }

}

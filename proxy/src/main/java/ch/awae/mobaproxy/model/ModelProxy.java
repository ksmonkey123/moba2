package ch.awae.mobaproxy.model;

import ch.awae.mobaproxy.LogHelper;
import ch.awae.mobaproxy.Sector;
import ch.awae.mobaproxy.event.EmitterRepository;
import ch.awae.mobaproxy.lights.LightButtonDecoder;
import ch.awae.mobaproxy.lights.LightCommand;
import ch.awae.mobaproxy.spi.SpiDataBundle;
import ch.awae.mobaproxy.switches.SwitchCommand;
import ch.awae.mobaproxy.event.Event;
import ch.awae.mobaproxy.event.EventService;
import ch.awae.mobaproxy.lights.LightModel;
import ch.awae.mobaproxy.switches.SwitchModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class ModelProxy {

    private final static Logger LOG = LogHelper.getLogger();

    private final EventService eventService;
    private final EmitterRepository emitterRepository;
    private final DisplayConfiguration displayConfiguration;
    private final LightButtonDecoder lightButtonDecoder;

    private final Map<Sector, SwitchModel> switchModels = new ConcurrentHashMap<>();
    private final LightModel lightModel;

    private Map<Sector, Integer> inputs = new ConcurrentHashMap<>();
    private boolean stealthMode = false;
    private boolean lastStealthButtonState = false;
    private AtomicLong lastCommand = new AtomicLong(0L);
    private long startupTracker = 0;

    public ModelProxy(
            EventService eventService,
            EmitterRepository emitterRepository,
            LightButtonDecoder lightButtonDecoder,
            DisplayConfiguration displayConfiguration) {
        this.eventService = eventService;
        this.emitterRepository = emitterRepository;
        this.lightButtonDecoder = lightButtonDecoder;
        this.displayConfiguration = displayConfiguration;

        for (Sector sector : Sector.values()) {
            if (sector != Sector.LIGHTS) {
                switchModels.put(sector, new SwitchModel());
            }
        }
        lightModel = new LightModel();
    }

    private Model<?> getModel(Sector sector) {
        if (sector != Sector.LIGHTS) {
            return switchModels.get(sector);
        } else {
            return lightModel;
        }
    }

    public SpiDataBundle getCommandBundle(Sector sector) {
        Model<?> model = getModel(sector);
        SpiDataBundle bundle = model.getNextBundle();

        if (stealthMode && model.getLastUpdate() + displayConfiguration.getStealthModeFadeOut() < System.currentTimeMillis()) {
            bundle.display = 0;
        }

        if (sector == Sector.LIGHTS) {
            int display = stealthMode ? 2 : 0;
            if (startupTracker == 0) {
                startupTracker = System.currentTimeMillis();
            }
            if (startupTracker != -1)
                if (startupTracker + 1000 > System.currentTimeMillis()) {
                    display = 2;
                } else {
                    startupTracker = -1;
            }
            if (emitterRepository.size() <= 0 || lastCommand.get() + displayConfiguration.getCommandFlashDuration() >= System.currentTimeMillis()) {
                display++;
            }
            // red status light is default-on in hardware
            display ^= 0x00000001;
            bundle.display = (short) (display & 0x000000ff);
        }

        return bundle;
    }

    public void updateSector(Sector sector, int input) {
        if (sector == Sector.LIGHTS) {
            updateStealthMode((input & 0x00000080) > 0);
        }
        input = (short) (input & sector.bitmask);
        Integer previous = inputs.put(sector, input);
        if (previous == null || previous != input) {
            if (sector == Sector.LIGHTS) {
                input = lightButtonDecoder.decode(input);
            }
            eventService.sendEvent(new Event(sector, 0x0000ffff & input));
        }
    }

    private void updateStealthMode(boolean pressed) {
        if (!lastStealthButtonState && pressed) {
            // rising edge of stealth button -> toggle
            stealthMode = !stealthMode;
        }
        lastStealthButtonState = pressed;
    }

    public void applySwitchCommand(Sector sector, SwitchCommand command) {
        Objects.requireNonNull(switchModels.get(sector)).update(command);
        LOG.info("processing switch command for sector " + sector + ": " + command);
        lastCommand.set(System.currentTimeMillis());
    }

    public void applyLightCommand(LightCommand command) {
        LOG.info("processing light command " + command);
        lightModel.update(command);
        lastCommand.set(System.currentTimeMillis());
    }

    public void longCommandLight() {
        lastCommand.set(System.currentTimeMillis() + 5000);
    }

}

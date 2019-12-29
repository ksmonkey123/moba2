package ch.awae.moba2.control;

import ch.awae.moba2.lights.LightModel;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ControlManager {

    private final static Logger log = Logger.getLogger(ControlManager.class.getName());

    private final LightModel lightModel;
    private final List<Controller> controllers;

    public ControlManager(ApplicationContext context, LightModel lightModel) {
        this.lightModel = lightModel;
        this.controllers = new ArrayList<>(context.getBeansOfType(Controller.class).values());

        log.info("loaded  " + this.controllers.size() + " controllers");
        for (Controller controller : this.controllers) {
            log.fine(" * " + controller.getClass());
        }
    }

    @Scheduled(fixedDelay = 50)
    public void tick() {
        controllers.forEach(Controller::tick);
        lightModel.flushChanges();
    }

}

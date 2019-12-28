package ch.awae.moba2.control;

import ch.awae.moba2.lights.LightModel;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log
@Component
public class ControlManager {

    private final LightModel lightModel;
    private final List<Controller> controllers;

    public ControlManager(ApplicationContext context, LightModel lightModel) {
        this.lightModel = lightModel;
        this.controllers = new ArrayList<>(context.getBeansOfType(Controller.class).values());

        log.info("loaded  " + this.controllers.size() + " controllers:");
        for (Controller controller : this.controllers) {
            log.info(" * " + controller.getClass());
        }
    }

    @Scheduled(fixedDelay = 50)
    public void tick() {
        controllers.forEach(Controller::tick);
        lightModel.flushChanges();
    }

}

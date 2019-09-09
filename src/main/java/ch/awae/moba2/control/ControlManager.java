package ch.awae.moba2.control;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ControlManager {

    private List<Controller> controllers;

    public ControlManager(ApplicationContext context) {
        controllers = new ArrayList<>(context.getBeansOfType(Controller.class).values());
    }

    @Scheduled(fixedDelay = 50)
    public void tick() {
        controllers.forEach(Controller::tick);
    }

}

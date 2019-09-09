package ch.awae.moba2.control;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ControlThread {

    private List<Controller> controllers;

    public ControlThread(ApplicationContext context) {
        controllers = new ArrayList<>(context.getBeansOfType(Controller.class).values());
    }

}

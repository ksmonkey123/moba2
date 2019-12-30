package ch.awae.moba2.control;

import ch.awae.moba2.lights.LightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProcessorService {

    private final static Logger log = Logger.getLogger(ProcessorService.class.getName());

    private final LightModel lightModel;
    private final List<Processor> processors;

    @Autowired
    public ProcessorService(ApplicationContext context, LightModel lightModel) {
        this.lightModel = lightModel;
        this.processors = new ArrayList<>(context.getBeansOfType(Processor.class).values());

        log.info("loaded  " + this.processors.size() + " processors");
        for (Processor processor : this.processors) {
            log.fine(" * " + processor.getClass());
        }
    }

    @Scheduled(fixedDelay = 50)
    public void tick() {
        processors.forEach(Processor::tick);
        lightModel.flushChanges();
    }

}

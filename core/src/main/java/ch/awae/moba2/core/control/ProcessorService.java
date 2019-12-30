package ch.awae.moba2.core.control;

import ch.awae.moba2.core.LogHelper;
import ch.awae.moba2.core.lights.LightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProcessorService {

    private final static Logger LOG = LogHelper.getLogger();

    private final LightModel lightModel;
    private final List<Processor> processors;

    @Autowired
    public ProcessorService(ApplicationContext context, LightModel lightModel) {
        this.lightModel = lightModel;
        this.processors = new ArrayList<>(context.getBeansOfType(Processor.class).values());

        LOG.info("loaded  " + this.processors.size() + " processors");
        for (Processor processor : this.processors) {
            LOG.fine(" * " + processor.getClass());
        }
    }

    @Scheduled(fixedDelay = 50)
    public void tick() {
        processors.forEach(Processor::tick);
        lightModel.flushChanges();
    }

}

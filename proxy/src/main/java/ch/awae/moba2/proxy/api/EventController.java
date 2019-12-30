package ch.awae.moba2.proxy.api;

import ch.awae.moba2.common.LogHelper;
import ch.awae.moba2.proxy.event.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.logging.Logger;

@RestController
public class EventController {

    private final static Logger LOG = LogHelper.getLogger();

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public SseEmitter getEventStream() {
        return eventService.createEmitter();
    }

}

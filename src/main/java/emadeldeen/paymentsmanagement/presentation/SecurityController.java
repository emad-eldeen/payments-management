package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.Event;
import emadeldeen.paymentsmanagement.business.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    @Autowired
    EventService eventService;

    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventService.listEvents();
    }
}

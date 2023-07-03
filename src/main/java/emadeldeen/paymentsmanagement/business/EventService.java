package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.persistence.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    HttpServletRequest context;


    public void createEvent(EventAction action, String subject,
                            String object) {
        Event event = new Event();
        event.setDate(LocalDate.now());
        event.setAction(action);
        event.setSubject(subject);
        event.setPath(context.getRequestURI());
        if (object == null) {
            event.object = event.path;
        } else {
            event.setObject(object);
        }
        eventRepository.save(event);
    }

    public void createEvent(EventAction action, UserDetails subject,
                            String object) {
        createEvent(action, subject == null ? "Anonymous" : subject.getUsername(),
                object);
    }

    public List<Event> listEvents() {
        List<Event> events = new ArrayList<>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }
}

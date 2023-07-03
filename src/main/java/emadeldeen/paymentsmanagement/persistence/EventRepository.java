package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {
}

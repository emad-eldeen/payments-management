package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.BreachedPassword;
import org.springframework.data.repository.CrudRepository;

public interface BreachedPasswordRepository extends CrudRepository<BreachedPassword, Long> {
    boolean existsByPassword(String password);
}

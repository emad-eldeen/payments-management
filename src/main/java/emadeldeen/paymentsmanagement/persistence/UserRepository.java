package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> getByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}

package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> getByRoleName(Role.RoleEnum roleName);
}

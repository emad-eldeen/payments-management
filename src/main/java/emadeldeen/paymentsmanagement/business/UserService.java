package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.exception.BadRequestException;
import emadeldeen.paymentsmanagement.exception.NotFoundException;
import emadeldeen.paymentsmanagement.persistence.RoleRepository;
import emadeldeen.paymentsmanagement.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    PasswordValidator passwordValidator;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EventService eventService;
    static final int MAX_FAILED_ATTEMPTS = 6;

    public User createUser(User user) {
        if (userRepository.getByEmailIgnoreCase(user.getEmail())
                .isPresent())
            throw new BadRequestException("User already exists!");
        PasswordValidator.PasswordValidationResult validationResult =
                passwordValidator.validatePassword(user.getPassword());
        if (!validationResult.valid())
            throw new BadRequestException(validationResult.message());
        // encode password
        user.setPassword(encoder.encode(user.getPassword()));
        // email to lower case
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);
    }

    public void changePassword(String email, String newPassword) {
        User user = userRepository.getByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadRequestException("User is not found"));
        PasswordValidator.PasswordValidationResult validationResult =
                passwordValidator.validateChangedPassword(newPassword, user.getPassword());
        if (!validationResult.valid())
            throw new BadRequestException(validationResult.message());
        // encode the new password and update the user
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getByEmailIgnoreCase(username);
    }

    public boolean userExists(String username) {
        return userRepository.existsByEmail(username);
    }

    private Role assignRole() {
        if (userRepository.count() == 0) {
            // The first registered user should receive the ADMINISTRATOR role
            return roleRepository.getByRoleName(Role.RoleEnum.ROLE_ADMINISTRATOR).
                    orElseThrow();
        } else {
            return roleRepository.getByRoleName(Role.RoleEnum.ROLE_USER).
                    orElseThrow();
        }
    }

    private boolean validateRole(Role.RoleEnum role) {
        if (role == null) {
            throw new BadRequestException("Invalid role");
        }
        if (userRepository.count() == 0) {
            // The first registered user should receive the ADMINISTRATOR role
            return role == Role.RoleEnum.ROLE_ADMINISTRATOR;
        } else {
            // admin already exists. admin is not allowed
            return role != Role.RoleEnum.ROLE_ADMINISTRATOR;
        }
    }

    public List<User> listUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.getByEmailIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        // if the user is admin
        if (user.getRolesString().contains(Role.RoleEnum.ROLE_ADMINISTRATOR.name())) {
            throw new BadRequestException("Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
    }

    public User changeUserRole(String username, String roleString,
                               Operation operation) {
        User dbUser = userRepository.getByEmailIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Role.RoleEnum roleEnum;
        try {
            roleEnum = Role.RoleEnum.valueOf("ROLE_" + roleString);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Role not found!");
        }
        Role dbRole = roleRepository.getByRoleName(roleEnum)
                .orElseThrow(() -> new BadRequestException("Role not found"));
        if (operation == Operation.REMOVE) {
            if (!dbUser.getRolesString().contains("ROLE_" + roleString))
                throw new BadRequestException("The user does not have a role!");
            if (dbRole.getRoleName() == Role.RoleEnum.ROLE_ADMINISTRATOR)
                throw new BadRequestException("Can't remove ADMINISTRATOR role!");
            if (dbUser.getRolesString().size() == 1)
                throw new BadRequestException("The user must have at least one role!");
        }
        if (operation == Operation.GRANT) {
            if (dbUser.getRolesString().contains("ROLE_" + roleString))
                throw new BadRequestException("The user already has this role!");
            if (roleEnum ==  Role.RoleEnum.ROLE_ADMINISTRATOR) {
                if (userHasBusinessRole(dbUser)) {
                    throw new BadRequestException("The user cannot combine administrative and business roles!");
                }
            } else { // adding business role
                if (userHasAdminRole(dbUser)) {
                    throw new BadRequestException("The user cannot combine administrative and business roles!");
                }
            }
        }
        switch (operation) {
            case GRANT -> dbUser.getRolesSet().add(dbRole);
            case REMOVE -> dbUser.getRolesSet().remove(dbRole);
        }
        userRepository.save(dbUser);
        return dbUser;
    }

    private boolean userHasAdminRole(User user) {
        return user.getRolesString().contains(Role.RoleEnum.ROLE_ADMINISTRATOR.name());
    }

    private boolean userHasBusinessRole(User user) {
        List<String> userRoles = user.getRolesString();
        return userRoles.contains(Role.RoleEnum.ROLE_USER.name()) ||
                userRoles.contains(Role.RoleEnum.ROLE_ACCOUNTANT.name()) ||
                userRoles.contains(Role.RoleEnum.ROLE_AUDITOR.name());
    }

    public void lockUser(String email) {
        User dbUser = userRepository.getByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        if (userHasAdminRole(dbUser))
            throw new BadRequestException("Can't lock the ADMINISTRATOR!");
        dbUser.setLocked(true);
        userRepository.save(dbUser);
    }

    public void unlockUser(String email) {
        User dbUser = userRepository.getByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        dbUser.setLocked(false);
        dbUser.setFailedAttempts(0);
        userRepository.save(dbUser);
    }

    public void failedUserLogin(String email) {
        eventService.createEvent(EventAction.LOGIN_FAILED, email, null);
        userRepository.getByEmailIgnoreCase(email).ifPresent(user -> {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.failedAttempts > MAX_FAILED_ATTEMPTS) {
                eventService.createEvent(EventAction.BRUTE_FORCE, email, null);
                user.setLocked(true);
                eventService.createEvent(EventAction.LOCK_USER, email, "Lock user " + email);
            }
            userRepository.save(user);
        });
    }
}

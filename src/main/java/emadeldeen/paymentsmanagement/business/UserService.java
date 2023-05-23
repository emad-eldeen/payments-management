package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.exception.BadRequestException;
import emadeldeen.paymentsmanagement.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    PasswordValidator passwordValidator;

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
}

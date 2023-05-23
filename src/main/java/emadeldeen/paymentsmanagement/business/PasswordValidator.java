package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.persistence.BreachedPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {
    static final int MINIMUM_LENGTH = 12;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    BreachedPasswordRepository breachedPasswordRepository;

    public PasswordValidationResult validatePassword(String password) {
        if (!lengthIsValid(password))
            return new PasswordValidationResult(false,
                    "Password length must be " + MINIMUM_LENGTH + " chars minimum!");
        if (passwordIsBreached(password))
            return new PasswordValidationResult(false,
                    "The password is in the hacker's database!");
        return new PasswordValidationResult(true, "");
    }

    private boolean lengthIsValid(String password) {
        return password.trim().length() >= MINIMUM_LENGTH;
    }

    private boolean passwordIsBreached(String password) {
        return breachedPasswordRepository.existsByPassword(password);
    }

    private boolean passwordsMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public PasswordValidationResult validateChangedPassword(String newPassword,
                                                            String oldEncodedPassword) {
        if (passwordsMatch(newPassword, oldEncodedPassword))
            return new PasswordValidationResult(false, "The passwords must be different!");
        return validatePassword(newPassword);
    }

    record PasswordValidationResult(boolean valid, String message) {}
}

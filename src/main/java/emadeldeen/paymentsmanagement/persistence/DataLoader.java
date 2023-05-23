package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.BreachedPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// a component to load static data into DB
@Component
public class DataLoader {

    static final List<String> BREACHED_PASSWORDS = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    );

    private final BreachedPasswordRepository breachedPasswordRepository;

    @Autowired
    public DataLoader(BreachedPasswordRepository groupRepository) {
        this.breachedPasswordRepository = groupRepository;
        initiateData();
    }

    private void initiateData() {
        // add items only if DB is empty
        if (breachedPasswordRepository.count() != 0)
            return;
        for (String password : BREACHED_PASSWORDS) {
            breachedPasswordRepository.save(new BreachedPassword(password));
        }
    }
}

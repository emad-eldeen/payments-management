package emadeldeen.paymentsmanagement.business;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class BreachedPassword {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    final String password;
}

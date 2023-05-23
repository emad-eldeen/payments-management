package emadeldeen.paymentsmanagement.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS") // User is a reserved keyword in H2
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotEmpty
    String name;
    @NotEmpty @JsonProperty("lastname")
    String lastName;
    @Email @NotEmpty
    String email;
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // to not send the password in JSON response
    String password;
}

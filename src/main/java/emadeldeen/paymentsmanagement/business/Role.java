package emadeldeen.paymentsmanagement.business;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    long id;
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    RoleEnum roleName;

    public enum RoleEnum {
        ROLE_ACCOUNTANT,
        ROLE_ADMINISTRATOR,
        ROLE_USER
    }
}

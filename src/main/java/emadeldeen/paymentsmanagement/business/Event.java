package emadeldeen.paymentsmanagement.business;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDate date;
    @Enumerated(EnumType.STRING)
    EventAction action;
    String subject;
    String object;
    String path;
}

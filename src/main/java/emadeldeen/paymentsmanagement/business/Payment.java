package emadeldeen.paymentsmanagement.business;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"name", "lastname","period", "salary"})
public class Payment {
    public static final String PERIOD_FORMAT = "MM-yyyy";
    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    String employee;
    @Min(0)
    long salary;
    @DateTimeFormat(pattern = PERIOD_FORMAT)
    YearMonth period;
    @JsonIgnore
    @ManyToOne()
    User user;

    @JsonSetter("period")
    public void setPeriodByString(String stringPeriod) {
        period = YearMonth.parse(stringPeriod, DateTimeFormatter.ofPattern(PERIOD_FORMAT));
    }

    @JsonGetter("period")
    public String getFormattedPeriod() {
        return period.format(DateTimeFormatter.ofPattern(PERIOD_FORMAT));
    }

    @JsonGetter("salary")
    public String getFormattedSalary() {
        return "%d dollar(s) %d cent(s)".formatted(salary / 100, salary % 100);
    }

    @JsonGetter("name")
    public String getName() {
        return user.getName();
    }

    @JsonGetter("lastname")
    public String getLastName() {
        return user.getLastName();
    }
}

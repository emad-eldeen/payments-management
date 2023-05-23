package emadeldeen.paymentsmanagement.persistence;

import emadeldeen.paymentsmanagement.business.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long>, PagingAndSortingRepository<Payment, Long> {
    Optional<Payment> getByUser_EmailAndPeriod(String userEmail, YearMonth period);
    List<Payment> findByUser_EmailOrderByIdDesc(String userEmail);
}

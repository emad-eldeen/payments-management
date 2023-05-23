package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.persistence.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentValidator {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UserService userService;

    public PaymentValidationResult validateAddPayment(Payment payment) {
        if (!userService.userExists(payment.employee))
            return new PaymentValidationResult(false, "user was not found");
        if (!paymentIsUnique(payment))
            return new PaymentValidationResult(false, "period is not unique");
        return new PaymentValidationResult(true, "");
    }

    public PaymentValidationResult validateChangePayment(Payment payment) {
        Optional<Payment> dbPayment = paymentRepository.getByUser_EmailAndPeriod(
                payment.employee, payment.period
        );
        if (dbPayment.isEmpty())
            return new PaymentValidationResult(false, "not found");
        dbPayment.get().setSalary(payment.getSalary());
        paymentRepository.save(dbPayment.get());
        return new PaymentValidationResult(true, "");
    }

    // it should not be possible to allocate the money more than once during the same period
    private boolean paymentIsUnique(Payment payment) {
        return paymentRepository.getByUser_EmailAndPeriod(payment.employee, payment.period)
                .isEmpty();
    }


    record PaymentValidationResult(boolean valid, String message) {
    }
}

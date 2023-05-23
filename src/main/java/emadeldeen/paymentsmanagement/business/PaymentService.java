package emadeldeen.paymentsmanagement.business;

import emadeldeen.paymentsmanagement.exception.BadRequestException;
import emadeldeen.paymentsmanagement.persistence.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentValidator paymentValidator;
    @Autowired
    UserService userService;

    @Transactional
    public void addPayments(List<Payment> paymentList) {
        for (Payment payment : paymentList) {
            PaymentValidator.PaymentValidationResult validationResult =
                    paymentValidator.validateAddPayment(payment);
            if (validationResult.valid()) {
                payment.setUser(userService.getUserByUsername(payment.employee)
                        .orElseThrow(() ->
                                new BadRequestException(validationResult.message())));
                paymentRepository.save(payment);
            } else {
                throw new BadRequestException(validationResult.message());
            }
        }
    }

    public void changePayment(Payment payment) {
        PaymentValidator.PaymentValidationResult validationResult =
                paymentValidator.validateChangePayment(payment);
        if (validationResult.valid()) {
            paymentRepository.save(payment);
        } else {
            throw new BadRequestException(validationResult.message());
        }
    }

    public Payment getPayment(String username, YearMonth period) {
        return paymentRepository.getByUser_EmailAndPeriod(username, period)
                .orElseThrow(() -> new BadRequestException("not found!"));
    }

    public List<Payment> getUserPayments(String username) {
        return paymentRepository.findByUser_EmailOrderByIdDesc(username);
    }
}

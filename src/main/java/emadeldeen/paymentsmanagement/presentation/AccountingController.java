package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.Payment;
import emadeldeen.paymentsmanagement.business.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounting")
public class AccountingController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/payments")
    public Object addPayments(@RequestBody List<@Valid Payment> paymentList) {
        paymentService.addPayments(paymentList);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping("/payments")
    public Object changePayment(@RequestBody @Valid Payment payment) {
        paymentService.changePayment(payment);
        return Map.of("status", "Updated successfully!");
    }
}

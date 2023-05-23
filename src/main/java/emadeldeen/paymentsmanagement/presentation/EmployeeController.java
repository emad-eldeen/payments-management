package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.Payment;
import emadeldeen.paymentsmanagement.business.PaymentService;
import emadeldeen.paymentsmanagement.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

    @GetMapping("/payment")
    public Object getPayments(@AuthenticationPrincipal UserDetails authenticatedUser,
                              @RequestParam(required = false) String period) {
        if (period == null) {
            return paymentService.getUserPayments(authenticatedUser.getUsername());
        } else {
            try {
                YearMonth yearMonth = YearMonth.parse(period, DateTimeFormatter.ofPattern(Payment.PERIOD_FORMAT));
                return paymentService.getPayment(authenticatedUser.getUsername(), yearMonth);
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }
}

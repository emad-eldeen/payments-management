package emadeldeen.paymentsmanagement.security;

import emadeldeen.paymentsmanagement.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken token) {
            try {
                String userEmail = (String) token.getPrincipal();
                userService.failedUserLogin(userEmail);
            }
            catch (RuntimeException ignored) {}
        }
    }
}

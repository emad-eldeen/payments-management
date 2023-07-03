package emadeldeen.paymentsmanagement.security;

import emadeldeen.paymentsmanagement.business.EventAction;
import emadeldeen.paymentsmanagement.business.EventService;
import emadeldeen.paymentsmanagement.business.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    EventService eventService;
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            UserDetailsImp user = (UserDetailsImp) auth.getPrincipal();
            eventService.createEvent(EventAction.ACCESS_DENIED, user.getUsername(), null);
            response.sendError(403, "Access Denied!");
        }
    }
}

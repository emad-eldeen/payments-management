package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.EventAction;
import emadeldeen.paymentsmanagement.business.EventService;
import emadeldeen.paymentsmanagement.business.User;
import emadeldeen.paymentsmanagement.business.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;
    @PostMapping("/users")
    public User createUser(@RequestBody @Valid User user,
                           @AuthenticationPrincipal UserDetails authenticatedUser){
        User createdUser = userService.createUser(user);
        eventService.createEvent(EventAction.CREATE_USER, authenticatedUser,
                createdUser.getEmail());
        return createdUser;
    }

    @PostMapping("/change-password")
    public Object changePassword(@AuthenticationPrincipal UserDetails authenticatedUser,
                                 @RequestBody ChangePassReq req) {
        userService.changePassword(authenticatedUser.getUsername(), req.new_password);
        eventService.createEvent(EventAction.CHANGE_PASSWORD, authenticatedUser,
                authenticatedUser.getUsername());
        return Map.of("email", authenticatedUser.getUsername(), "status",
                "The password has been updated successfully");
    }

    record ChangePassReq(String new_password){}
}

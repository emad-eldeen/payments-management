package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;

    @GetMapping("/user")
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @DeleteMapping("/user")
    public Object deleteUser(@RequestBody @Valid DeleteUserRequest requestBody,
                             @AuthenticationPrincipal UserDetails authenticatedUser) {
        userService.deleteUserByUsername(requestBody.user);
        eventService.createEvent(EventAction.DELETE_USER, authenticatedUser, requestBody.user);
        return Map.of(
                "user", requestBody.user,
                "status", "Deleted successfully!"
        );
    }

    @PutMapping("/user/role")
    public User changeRole(@RequestBody @Valid ChangeRoleRequest requestBody,
                           @AuthenticationPrincipal UserDetails authenticatedUser) {
        User user = userService.changeUserRole(requestBody.user, requestBody.role, requestBody.operation);
        switch (requestBody.operation) {
            case GRANT -> eventService.createEvent(EventAction.GRANT_ROLE, authenticatedUser,
                    "Grant role %s to %s".formatted(requestBody.role, requestBody.user.toLowerCase()));
            case REMOVE -> eventService.createEvent(EventAction.REMOVE_ROLE, authenticatedUser,
                    "Remove role %s from %s".formatted(requestBody.role, requestBody.user.toLowerCase()));
        }
        return user;
    }

    @PutMapping("user/access")
    public Object lockUnlockUser(@RequestBody @Valid lockUnlockUserRequest request,
                                 @AuthenticationPrincipal UserDetails authenticatedUser) {
        switch (request.operation) {
            case LOCK -> {
                userService.lockUser(request.user);
                eventService.createEvent(EventAction.LOCK_USER, authenticatedUser,
                        "Lock user %s".formatted(request.user.toLowerCase()));
                return Map.of(
                        "status", "User " + request.user.toLowerCase() + " locked!"
                );
            }
            case UNLOCK -> {
                userService.unlockUser(request.user);
                eventService.createEvent(EventAction.UNLOCK_USER, authenticatedUser.getUsername(),
                        "Unlock user %s".formatted(request.user.toLowerCase()));
                return Map.of(
                        "status", "User " + request.user.toLowerCase() + " unlocked!"
                );
            }
            default -> throw new IllegalArgumentException("Invalid operation");
        }
    }

    record ChangeRoleRequest(@NotEmpty String user, @NotEmpty String role, Operation operation) {
    }

    record DeleteUserRequest(@NotEmpty String user){}
    record lockUnlockUserRequest(@NotEmpty String user, Operation operation){}
}

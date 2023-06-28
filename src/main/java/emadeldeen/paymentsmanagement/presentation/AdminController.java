package emadeldeen.paymentsmanagement.presentation;

import emadeldeen.paymentsmanagement.business.Operation;
import emadeldeen.paymentsmanagement.business.User;
import emadeldeen.paymentsmanagement.business.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/user")
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @DeleteMapping("/user")
    public Object deleteUser(@RequestBody @Valid DeleteUserRequest requestBody) {
        userService.deleteUserByUsername(requestBody.user);
        return Map.of(
                "user", requestBody.user,
                "status", "Deleted successfully!"
        );
    }

    @PutMapping("/user/role")
    public User changeRole(@RequestBody @Valid ChangeRoleRequest requestBody) {
        return userService.changeUserRole(requestBody.user, requestBody.role, requestBody.operation);
    }

    record ChangeRoleRequest(@NotEmpty String user, @NotEmpty String role, Operation operation) {
    }

    record DeleteUserRequest(@NotEmpty String user){}
}

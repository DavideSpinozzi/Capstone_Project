package davidespinozzi.CarGo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable UUID id) {
        return usersService.findById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody NewUserPayload body) {
        return usersService.findByIdAndUpdate(id, body);
    }
  
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        usersService.findByIdAndDelete(id);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserByEmail(@PathVariable String email) {
        return usersService.findByEmail(email);
    }
}


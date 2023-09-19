package davidespinozzi.CarGo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import davidespinozzi.CarGo.bookings.Booking;
import davidespinozzi.CarGo.bookings.Stato;
import davidespinozzi.CarGo.exceptions.NotFoundException;

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
    
    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser() {
        UUID currentUserId = getCurrentUserId();
        return usersService.findById(currentUserId);
    }

    @PutMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public User updateCurrentUser(@RequestBody NewUserPayload body) {
    	UUID currentUserId = getCurrentUserId();
        return usersService.findByIdAndUpdate(currentUserId, body);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(@PathVariable UUID id, @RequestBody NewUserPayload body) {
        return usersService.findByIdAndUpdate(id, body);
    }
  
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable UUID id) {
        usersService.findByIdAndDelete(id);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserByEmail(@PathVariable String email) {
        return usersService.findByEmail(email);
    }
    
    @GetMapping("/bookings/open")
    @PreAuthorize("isAuthenticated()")
    public List<Booking> getUserOpenBookings() {
        UUID currentUserId = getCurrentUserId();
        return usersService.findUserBookingsByState(currentUserId, Stato.APERTO);
    }

    @GetMapping("/bookings/closed")
    @PreAuthorize("isAuthenticated()")
    public List<Booking> getUserClosedBookings() {
        UUID currentUserId = getCurrentUserId();
        return usersService.findUserBookingsByState(currentUserId, Stato.CHIUSO);
    }
    
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }
}


package davidespinozzi.CarGo.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import davidespinozzi.CarGo.cars.CarService;
import davidespinozzi.CarGo.cars.Cars;
import davidespinozzi.CarGo.exceptions.NotAvailableException;
import davidespinozzi.CarGo.exceptions.NotFoundException;
import davidespinozzi.CarGo.user.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingPayload bookingPayload) throws NotFoundException, NotAvailableException {
        return new ResponseEntity<>(bookingService.createBooking(bookingPayload), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<Booking> getAllBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/{id}")
    
    public Booking getBookingById(@PathVariable UUID id) throws NotFoundException {
        return bookingService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable UUID id, @RequestBody BookingPayload bookingPayload) throws NotFoundException, NotAvailableException {
        return new ResponseEntity<>(bookingService.updateBooking(id, bookingPayload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable UUID id) throws NotFoundException {
        bookingService.findByIdAndDelete(id);
    }


    @PutMapping("/{id}/close")
    public ResponseEntity<Booking> closeBooking(@PathVariable UUID id) throws NotFoundException {
        return new ResponseEntity<>(bookingService.changeBookingState(id, Stato.CHIUSO), HttpStatus.OK);
    }
}


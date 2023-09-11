package davidespinozzi.CarGo.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import davidespinozzi.CarGo.cars.CarService;
import davidespinozzi.CarGo.cars.Cars;
import davidespinozzi.CarGo.exceptions.NotAvailableException;
import davidespinozzi.CarGo.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingPayload bookingPayload) {
        try {
            Cars car = bookingPayload.getCar();
            List<Booking> overlappingBookings = car.getBook().stream()
                    .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), bookingPayload.getDataInizio(), bookingPayload.getDataFine()))
                    .collect(Collectors.toList());

            if (!overlappingBookings.isEmpty()) {
                throw new NotAvailableException("Date prenotate non disponibili per questa auto.");
            }

            Booking newBooking = bookingService.save(bookingPayload);
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (NotAvailableException e) {
            throw e;
        }
    }
    
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable UUID id) throws NotFoundException {
        return bookingService.findById(id);
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable UUID id, @RequestBody BookingPayload bookingPayload) throws NotFoundException {
        return bookingService.findByIdAndUpdate(id, bookingPayload);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable UUID id) throws NotFoundException {
        bookingService.findByIdAndDelete(id);
    }
    
    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return !startDate1.isAfter(endDate2) && !startDate2.isAfter(endDate1);
    }
}

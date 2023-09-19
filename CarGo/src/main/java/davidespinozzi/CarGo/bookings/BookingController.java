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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private CarService carService;

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingPayload bookingPayload) {
        try {
      
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            System.out.println(currentUser);

            UUID carId = bookingPayload.getCarId();

            Cars car = carService.findById(carId);

            if (car == null) {
                throw new NotFoundException("Auto non trovata.");
            }
            LocalDate dataInizio = bookingPayload.getDataInizio();
            LocalDate dataFine = bookingPayload.getDataFine();

            if (dataInizio == null || dataFine == null) {
                throw new IllegalArgumentException("Le date non possono essere nulle");
            }

            List<Booking> overlappingBookings = car.getBookings().stream()
                    .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), dataInizio, dataFine))
                    .collect(Collectors.toList());

            if (!overlappingBookings.isEmpty()) {
                throw new NotAvailableException("Date prenotate non disponibili per questa auto.");
            }
            Booking newBooking = new Booking(bookingPayload.getDataInizio(), bookingPayload.getDataFine());
            newBooking.setCar(car);
            newBooking.setUser(currentUser);
            newBooking.setStato(Stato.APERTO);
            currentUser.getBookings().add(newBooking);
            newBooking = bookingService.save(newBooking);

            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (NotAvailableException | NotFoundException e) {
            throw e;
        }
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Booking> getAllBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    
    @PutMapping("/{id}/close")
    public ResponseEntity<Booking> closeBooking(@PathVariable UUID id) throws NotFoundException {
        Booking updatedBooking = bookingService.changeBookingState(id, Stato.CHIUSO);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        if (startDate1 == null || endDate1 == null || startDate2 == null || endDate2 == null) {
            throw new IllegalArgumentException("Le date non possono essere nulle");
        }

        return (startDate1.isEqual(startDate2) || startDate1.isAfter(startDate2)) && startDate1.isBefore(endDate2) ||
               (endDate1.isEqual(endDate2) || endDate1.isBefore(endDate2)) && endDate1.isAfter(startDate2) ||
               (startDate2.isEqual(startDate1) || startDate2.isAfter(startDate1)) && startDate2.isBefore(endDate1) ||
               (endDate2.isEqual(endDate1) || endDate2.isBefore(endDate1)) && endDate2.isAfter(startDate1);
    }
    
}

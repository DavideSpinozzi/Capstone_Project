package davidespinozzi.CarGo.bookings;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import davidespinozzi.CarGo.cars.CarService;
import davidespinozzi.CarGo.cars.Cars;
import davidespinozzi.CarGo.exceptions.NotAvailableException;
import davidespinozzi.CarGo.exceptions.NotFoundException;
import davidespinozzi.CarGo.user.User;
import davidespinozzi.CarGo.user.UsersService;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CarService carService;
    
    @Autowired
    UsersService userService;

    public Booking createBooking(BookingPayload bookingPayload) throws NotFoundException, NotAvailableException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UUID carId = bookingPayload.getCarId();
        Cars car = carService.getCarById(carId);

        if (car == null) throw new NotFoundException("Auto non trovata.");

        LocalDate dataInizio = bookingPayload.getDataInizio();
        LocalDate dataFine = bookingPayload.getDataFine();

        if (dataInizio == null || dataFine == null) throw new IllegalArgumentException("Le date non possono essere nulle");
        if (dataInizio.isBefore(LocalDate.now()) || dataFine.isBefore(LocalDate.now())) throw new IllegalArgumentException("Le date non possono essere precedenti alla data corrente");
        
        List<Booking> overlappingBookingsUser = currentUser.getBookings().stream()
                .filter(booking -> booking.getCar().getId().equals(carId))
                .filter(booking -> booking.getStato() == Stato.APERTO)
                .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), dataInizio, dataFine))
                .collect(Collectors.toList());

        if (!overlappingBookingsUser.isEmpty()) throw new NotAvailableException("Date inserite non disponibili per questa auto.");

        List<Booking> overlappingBookingsCar = car.getBookings().stream()
                .filter(booking -> booking.getStato() == Stato.CHIUSO)
                .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), dataInizio, dataFine))
                .collect(Collectors.toList());

        if (!overlappingBookingsCar.isEmpty()) throw new NotAvailableException("Date prenotate non disponibili per questa auto.");

        long daysBetween = ChronoUnit.DAYS.between(dataInizio, dataFine);
        Booking newBooking = new Booking(dataInizio, dataFine);
        newBooking.setNomeModello(car.getModello());
        newBooking.setEmailAcquirente(currentUser.getEmail());
        newBooking.setCostoTotale(daysBetween * car.getCostoGiornaliero());
        newBooking.setCar(car);
        newBooking.setUser(currentUser);
        newBooking.setStato(Stato.APERTO);
        currentUser.getBookings().add(newBooking);
        car.getBookings().add(newBooking);

        return save(newBooking);
    }


    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking findById(UUID id) throws NotFoundException {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Booking updateBooking(UUID id, BookingPayload bookingPayload) throws NotFoundException, NotAvailableException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Booking existingBooking = findById(id);
        Cars car = carService.getCarById(existingBooking.getCar().getId());

        if (car == null) throw new NotFoundException("Auto non trovata.");

        LocalDate dataInizio = bookingPayload.getDataInizio();
        LocalDate dataFine = bookingPayload.getDataFine();

        if (dataInizio == null || dataFine == null) throw new IllegalArgumentException("Le date non possono essere nulle");
        if (dataInizio.isBefore(LocalDate.now()) || dataFine.isBefore(LocalDate.now())) throw new IllegalArgumentException("Le date non possono essere precedenti alla data corrente");
        
        UUID carId = existingBooking.getCar().getId();
        List<Booking> overlappingBookingsUser = currentUser.getBookings().stream()
        		.filter(booking -> !booking.getId().equals(id))
                .filter(booking -> booking.getCar().getId().equals(carId))
                .filter(booking -> booking.getStato() == Stato.APERTO)
                .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), dataInizio, dataFine))
                .collect(Collectors.toList());

        if (!overlappingBookingsUser.isEmpty()) throw new NotAvailableException("Date inserite non disponibili per questa auto.");

        List<Booking> overlappingBookings = car.getBookings().stream()
        		.filter(booking -> !booking.getId().equals(id))
                .filter(booking -> booking.getStato() == Stato.CHIUSO)
                .filter(booking -> isDateOverlap(booking.getDataInizio(), booking.getDataFine(), dataInizio, dataFine))
                .collect(Collectors.toList());

        if (!overlappingBookings.isEmpty()) throw new NotAvailableException("Date prenotate non disponibili per questa auto.");

        long daysBetween = ChronoUnit.DAYS.between(dataInizio, dataFine);
        existingBooking.setDataInizio(dataInizio);
        existingBooking.setDataFine(dataFine);
        existingBooking.setCostoTotale(daysBetween * car.getCostoGiornaliero());
        return save(existingBooking);
    }


    public void findByIdAndDelete(UUID id) {
        Booking booking = findById(id);
        User user = userService.findById(booking.getUser().getId());
        Cars car = carService.getCarById(booking.getCar().getId());
        user.getBookings().remove(booking);
        car.getBookings().remove(booking);

        userService.save(user);
        carService.save(car);

        bookingRepository.delete(booking);
    }

    public Booking changeBookingState(UUID id, Stato newState) throws NotFoundException {
        Booking found = findById(id);
        found.setStato(newState);
        return save(found);
    }

    public boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        if (startDate1 == null || endDate1 == null || startDate2 == null || endDate2 == null)
            throw new IllegalArgumentException("Le date non possono essere nulle");

        return (startDate1.isEqual(startDate2) || startDate1.isAfter(startDate2)) && startDate1.isBefore(endDate2) ||
               (endDate1.isEqual(endDate2) || endDate1.isBefore(endDate2)) && endDate1.isAfter(startDate2) ||
               (startDate2.isEqual(startDate1) || startDate2.isAfter(startDate1)) && startDate2.isBefore(endDate1) ||
               (endDate2.isEqual(endDate1) || endDate2.isBefore(endDate1)) && endDate2.isAfter(startDate1);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
}

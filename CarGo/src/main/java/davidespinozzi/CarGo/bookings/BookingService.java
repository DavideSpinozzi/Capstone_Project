package davidespinozzi.CarGo.bookings;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import davidespinozzi.CarGo.exceptions.NotFoundException;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public Booking save(BookingPayload body) {
        Booking newBooking = new Booking(body.getDataInizio(), body.getDataFine());
        return bookingRepository.save(newBooking);
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public Booking findById(UUID id) throws NotFoundException {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Booking findByIdAndUpdate(UUID id, BookingPayload body) throws NotFoundException {
        Booking found = this.findById(id);
        found.setDataInizio(body.getDataInizio());
        found.setDataFine(body.getDataFine());
        return bookingRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) throws NotFoundException {
        Booking found = this.findById(id);
        bookingRepository.delete(found);
    }
}
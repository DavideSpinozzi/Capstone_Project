package davidespinozzi.CarGo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import davidespinozzi.CarGo.bookings.Booking;
import davidespinozzi.CarGo.bookings.BookingRepository;
import davidespinozzi.CarGo.bookings.Stato;
import davidespinozzi.CarGo.exceptions.NotFoundException;
import davidespinozzi.CarGo.user.UsersService;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class CarGoApplication implements CommandLineRunner {
    
    @Autowired
    UsersService userService;

    @Autowired
    BookingRepository bookingRepository;
    
    public static void main(String[] args) {
        SpringApplication.run(CarGoApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
    	//UUID id = UUID.fromString("9f366823-428e-422e-a3d9-4a9c720b343b");
        //userService.changeRole(id);
    	
    	List<Booking> allBookings = bookingRepository.findAll();
    	System.out.println(allBookings);
    	for (Booking booking : allBookings) {
    	    if (booking.getDataFine().isBefore(LocalDate.now()) && booking.getStato().equals(Stato.APERTO)) {
    	    	System.out.println(booking);
    	    	Booking bookingDel = bookingRepository.findById(booking.getId()).orElseThrow(() -> new NotFoundException(booking.getId()));
    	        bookingRepository.delete(bookingDel);
    	    }
    	}
    }
}


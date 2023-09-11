package davidespinozzi.CarGo.bookings;

import java.time.LocalDate;
import davidespinozzi.CarGo.cars.Cars;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class BookingPayload {
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private Cars car;
}
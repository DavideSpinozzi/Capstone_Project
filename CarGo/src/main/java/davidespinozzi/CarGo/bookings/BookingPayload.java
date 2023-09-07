package davidespinozzi.CarGo.bookings;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingPayload {
	private LocalDate dataInizio;
	private LocalDate dataFine;
}

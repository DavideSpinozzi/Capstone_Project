package davidespinozzi.CarGo.bookings;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import davidespinozzi.CarGo.cars.Cars;
import davidespinozzi.CarGo.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@Data
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Double costoTotale;
    @Enumerated(EnumType.STRING)
    private Stato stato;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
     private User user;
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonBackReference
     private Cars car;

    public Booking(LocalDate dataInizio, LocalDate dataFine) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
    
    @Override
	public String toString() {
		return "data d'inizio = " + dataInizio + " data fine= " + dataFine;
	}

}
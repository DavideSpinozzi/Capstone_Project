package davidespinozzi.CarGo.cars;

import java.util.Set;
import java.util.UUID;

import davidespinozzi.CarGo.bookings.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String marca;
    private String modello;
    private String colore;
    private String motore;
    private String cilindrata;
    private String potenza;
    private String tipoDiAlimentazione;
    private String consumoAKm;
    private Double costoOrario;

    @OneToMany(mappedBy = "car")
    private Set<Booking> book;

    public Cars(String marca, String modello, String colore, String motore, String cilindrata, String potenza,
                String tipoDiAlimentazione, String consumoAKm, Double costoOrario) {
        this.marca = marca;
        this.modello = modello;
        this.colore = colore;
        this.motore = motore;
        this.cilindrata = cilindrata;
        this.potenza = potenza;
        this.tipoDiAlimentazione = tipoDiAlimentazione;
        this.consumoAKm = consumoAKm;
        this.costoOrario = costoOrario;
    }
}

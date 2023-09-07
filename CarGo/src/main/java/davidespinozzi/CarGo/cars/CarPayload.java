package davidespinozzi.CarGo.cars;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CarPayload {

	private String marca;
	private String modello;
	private String colore;
	private String motore;
	private String cilindrata;
	private String potenza;
	private String tipoDiAlimentazione;
	private String consumoAKm;
	private Double costoOrario;
}

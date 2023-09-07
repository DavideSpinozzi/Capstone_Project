package davidespinozzi.CarGo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@ComponentScan(basePackages = "davidespinozzi.CarGo")
@EntityScan(basePackages = {"davidespinozzi.CarGo.cars.Cars", "davidespinozzi.CarGo.users.User", "davidespinozzi.CarGo.bookings.Booking"})
public class CarGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarGoApplication.class, args);
	}

}

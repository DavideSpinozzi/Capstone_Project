package davidespinozzi.CarGo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import davidespinozzi.CarGo.user.UsersService;

@SpringBootApplication
public class CarGoApplication implements CommandLineRunner {
    
    @Autowired
    UsersService userService;

    public static void main(String[] args) {
        SpringApplication.run(CarGoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	//UUID id = UUID.fromString("9f366823-428e-422e-a3d9-4a9c720b343b");
        //userService.changeRole(id);
    }
}


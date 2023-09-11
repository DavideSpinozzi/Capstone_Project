package davidespinozzi.CarGo.cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import davidespinozzi.CarGo.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
public class CarsController {

    @Autowired
    private CarService carService;

    @PostMapping
    public Cars createCar(@RequestBody CarPayload carPayload) {
        return carService.save(carPayload);
    }

    @GetMapping
    public List<Cars> getAllCars() {
        return carService.getCars();
    }

    @GetMapping("/{id}")
    public Cars getCarById(@PathVariable UUID id) throws NotFoundException {
        return carService.findById(id);
    }

    @PutMapping("/{id}")
    public Cars updateCar(@PathVariable UUID id, @RequestBody CarPayload carPayload) throws NotFoundException {
        return carService.findByIdAndUpdate(id, carPayload);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable UUID id) throws NotFoundException {
        carService.findByIdAndDelete(id);
    }
}

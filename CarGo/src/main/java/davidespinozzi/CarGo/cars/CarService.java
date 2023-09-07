package davidespinozzi.CarGo.cars;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import davidespinozzi.CarGo.exceptions.NotFoundException;


@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    public Cars save(CarPayload body) {
        Cars newCar = new Cars(body.getMarca(), body.getModello(), body.getColore(),
                body.getMotore(), body.getCilindrata(), body.getPotenza(),
                body.getTipoDiAlimentazione(), body.getConsumoAKm(), body.getCostoOrario());
        return carRepository.save(newCar);
    }

    public List<Cars> getCars() {
        return carRepository.findAll();
    }

    public Cars findById(UUID id) throws NotFoundException {
        return carRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Cars findByIdAndUpdate(UUID id, CarPayload body) throws NotFoundException {
        Cars found = this.findById(id);
        found.setMarca(body.getMarca());
        found.setModello(body.getModello());
        found.setColore(body.getColore());
        found.setMotore(body.getMotore());
        found.setCilindrata(body.getCilindrata());
        found.setPotenza(body.getPotenza());
        found.setTipoDiAlimentazione(body.getTipoDiAlimentazione());
        found.setConsumoAKm(body.getConsumoAKm());
        found.setCostoOrario(body.getCostoOrario());
        return carRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) throws NotFoundException {
        Cars found = this.findById(id);
        carRepository.delete(found);
    }
}
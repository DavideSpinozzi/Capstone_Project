package davidespinozzi.CarGo.cars;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Cars, UUID>{

    List<Cars> findByMarca(String marca);
    
    List<Cars> findByModello(String modello);
    
    List<Cars> findByColore(String colore);
    
}
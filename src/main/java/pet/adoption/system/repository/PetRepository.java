package pet.adoption.system.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pet.adoption.system.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
  List<Pet> findByAdoptedFalse();
  List<Pet> findByShelter_Id(Long shelterId);
}
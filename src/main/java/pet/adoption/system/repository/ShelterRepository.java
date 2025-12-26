package pet.adoption.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.adoption.system.entity.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
   boolean existsByName(String name);
}

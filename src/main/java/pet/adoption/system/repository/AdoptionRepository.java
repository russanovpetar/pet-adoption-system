package pet.adoption.system.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pet.adoption.system.entity.AdoptionApplication;

public interface AdoptionRepository extends JpaRepository<AdoptionApplication, Long> {
  List<AdoptionApplication> findByPetShelterId(Long shelterId);
  List<AdoptionApplication> findByAdopterId(Long adopterId);
  List<AdoptionApplication> findByPet_IdAndStatus(Long petId, AdoptionApplication.Status status);

  void deleteByPet_Id(Long petId);
}

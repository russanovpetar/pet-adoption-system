package pet.adoption.system.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.ShelterRequest;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.exception.ShelterAlreadyExistsException;
import pet.adoption.system.repository.ShelterRepository;

@Service
public class ShelterService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);
  private final ShelterRepository shelterRepo;

  public ShelterService(ShelterRepository shelterRepo) {
    this.shelterRepo = shelterRepo;
  }

  public List<Shelter> listShelters() {
    return shelterRepo.findAll();
  }

  public Shelter getShelterById(Long id) {
    return shelterRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Shelter not found"));
  }

  public void createShelter(ShelterRequest request) {
    if (shelterRepo.existsByName(request.getName())) {
      LOGGER.warn("Registration failed - Shelter already exists.");
      throw new ShelterAlreadyExistsException();
    }

    Shelter shelter = new Shelter();
    shelter.setName(request.getName());
    shelter.setLocation(request.getLocation());

    shelterRepo.save(shelter);
  }
}

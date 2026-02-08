package pet.adoption.system.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.ShelterRequest;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.ShelterAlreadyExistsException;
import pet.adoption.system.exception.ShelterHasPetsException;
import pet.adoption.system.exception.ShelterNotFoundException;
import pet.adoption.system.repository.PetRepository;
import pet.adoption.system.repository.ShelterRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class ShelterService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);
  private final ShelterRepository shelterRepo;
  private final PetRepository petRepo;
  private final UserRepository userRepo;

  public ShelterService(ShelterRepository shelterRepo, PetRepository petRepo, UserRepository userRepo) {
    this.shelterRepo = shelterRepo;
    this.petRepo = petRepo;
    this.userRepo = userRepo;
  }

  public List<Shelter> listShelters() {
    return shelterRepo.findAll();
  }

  public Shelter getShelterById(Long id) {
    return shelterRepo.findById(id).orElseThrow(ShelterNotFoundException::new);
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

  public void deleteShelter(Long id) {
    Shelter shelter = shelterRepo.findById(id).orElseThrow(ShelterNotFoundException::new);

    if (!petRepo.findByShelter_Id(id).isEmpty()) {
      LOGGER.warn("Delete rejected - shelter has pets. shelterId={}", id);
      throw new ShelterHasPetsException();
    }

    List<User> staff = userRepo.findByShelter_IdOrderByUsernameAsc(id);
    for (User user : staff) {
      user.setShelter(null);
      userRepo.save(user);
    }

    shelterRepo.delete(shelter);
    LOGGER.info("Shelter deleted. id={}", id);
  }
}

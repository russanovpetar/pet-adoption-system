package pet.adoption.system.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.PetRequest;
import pet.adoption.system.entity.Pet;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.repository.PetRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class PetService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PetService.class);

  private final PetRepository petRepository;
  private final UserRepository userRepository;

  public PetService(PetRepository petRepository, UserRepository userRepository) {
    this.petRepository = petRepository;
    this.userRepository = userRepository;
  }

  public Pet addPet(PetRequest request, String username) {
    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    Shelter shelter = user.getShelter();

    if (shelter == null) {
      throw new IllegalStateException("Shelter staff is not assigned to a shelter");
    }

    Pet pet = new Pet();
    pet.setName(request.getName());
    pet.setSpecies(request.getSpecies());
    pet.setAge(request.getAge());
    pet.setShelter(shelter);

    Pet saved = petRepository.save(pet);

    LOGGER.info("Pet '{}' added to shelter '{}'", saved.getName(), shelter.getName());

    return saved;
  }

  public List<Pet> browsePets() {
    return petRepository.findByAdoptedFalse();
  }

  public Pet viewPet(Long id) {
    return petRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pet not found"));
  }
}


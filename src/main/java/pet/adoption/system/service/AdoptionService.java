package pet.adoption.system.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.AdoptionApplicationRequest;
import pet.adoption.system.dto.response.AdoptionApplicationResponse;
import pet.adoption.system.entity.AdoptionApplication;
import pet.adoption.system.entity.Pet;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.AdoptionApplicationNotFoundException;
import pet.adoption.system.exception.PetNotFoundException;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.repository.AdoptionRepository;
import pet.adoption.system.repository.PetRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class AdoptionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PetService.class);
  private final AdoptionRepository adoptionRepository;
  private final PetRepository petRepository;
  private final UserRepository userRepository;

  public AdoptionService(
      AdoptionRepository adoptionRepository,
      PetRepository petRepository,
      UserRepository userRepository) {
    this.adoptionRepository = adoptionRepository;
    this.petRepository = petRepository;
    this.userRepository = userRepository;
  }

  public List<AdoptionApplicationResponse> getAllApplications() {
    List<AdoptionApplication> apps = adoptionRepository.findAll();

    return apps.stream().map(AdoptionApplicationResponse::new).toList();
  }

  public List<AdoptionApplicationResponse> getApplicationsForAdopter(String username) {
    User adopter = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    List<AdoptionApplication> apps = adoptionRepository.findByAdopterId(adopter.getId());

    return apps.stream().map(AdoptionApplicationResponse::new).toList();
  }

  public AdoptionApplicationResponse applyForAdoption(AdoptionApplicationRequest request, String username) {
    User adopter = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    Pet pet = petRepository.findById(request.getPetId()).orElseThrow(PetNotFoundException::new);

    AdoptionApplication application = new AdoptionApplication();
    application.setPet(pet);
    application.setAdopter(adopter);
    application.setMessage(request.getMessage());

    AdoptionApplication saved = adoptionRepository.save(application);

    LOGGER.info("Adopter '{}' applied for pet '{}'", username, pet.getName());

    return new AdoptionApplicationResponse(saved);
  }

  public List<AdoptionApplicationResponse> getApplicationsForShelter(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    List<AdoptionApplication> apps = adoptionRepository.findByPetShelterId(user.getShelter().getId());

    return apps.stream().map(AdoptionApplicationResponse::new).toList();
  }

  public AdoptionApplicationResponse reviewApplication(
      Long applicationId,
      AdoptionApplication.Status status,
      String username) {

    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    AdoptionApplication app = adoptionRepository.findById(applicationId).orElseThrow(AdoptionApplicationNotFoundException::new);

    if (!app.getPet().getShelter().getId().equals(user.getShelter().getId())) {
      throw new AccessDeniedException("Cannot review applications for other shelters");
    }

    app.setStatus(status);
    AdoptionApplication updated = adoptionRepository.save(app);

    LOGGER.info("Application {} reviewed by {}: {}", applicationId, username, status);

    return new AdoptionApplicationResponse(updated);
  }
}

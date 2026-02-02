package pet.adoption.system.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pet.adoption.system.dto.request.PetRequest;
import pet.adoption.system.entity.Pet;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.PetNotFoundException;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.repository.AdoptionRepository;
import pet.adoption.system.repository.PetRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class PetService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PetService.class);

  @Value("${pet.upload-dir:uploads/pets}")
  private String uploadDir;

  private final PetRepository petRepository;
  private final UserRepository userRepository;
  private final AdoptionRepository adoptionRepository;

  public PetService(PetRepository petRepository, UserRepository userRepository,
      AdoptionRepository adoptionRepository) {
    this.petRepository = petRepository;
    this.userRepository = userRepository;
    this.adoptionRepository = adoptionRepository;
  }

  public Pet addPet(PetRequest request, String username, MultipartFile image) {
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

    if (image != null && !image.isEmpty()) {
      String imagePath = savePetImage(saved.getId(), image);
      if (imagePath != null) {
        saved.setImagePath(imagePath);
        saved = petRepository.save(saved);
      }
    }

    LOGGER.info("Pet '{}' added to shelter '{}'", saved.getName(), shelter.getName());

    return saved;
  }

  private String savePetImage(Long petId, MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (filename == null || filename.isBlank()) {
      return null;
    }
    String ext = filename.contains(".") ? filename.substring(filename.lastIndexOf('.')) : "";
    String savedFilename = petId + ext;

    try {
      Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
      Files.createDirectories(uploadPath);

      Path targetPath = uploadPath.resolve(savedFilename);
      try (InputStream is = file.getInputStream()) {
        Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
      }

      return "uploads/pets/" + savedFilename;
    } catch (IOException e) {
      LOGGER.warn("Failed to save pet image: {}", e.getMessage());
      return null;
    }
  }

  public List<Pet> browsePets() {
    return petRepository.findByAdoptedFalse();
  }

  public List<Pet> getPetsByShelterId(Long shelterId) {
    return petRepository.findByShelter_Id(shelterId);
  }

  public Pet viewPet(Long id) {
    return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
  }

  /**
   * Removes a pet from the system. Only staff of the shelter that owns the pet can remove it.
   * Deletes associated adoption applications and the pet's image file if present.
   */
  public void removePet(Long petId, String username) {
    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    Shelter userShelter = user.getShelter();
    if (userShelter == null) {
      throw new IllegalStateException("Shelter staff is not assigned to a shelter");
    }

    Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
    if (!userShelter.getId().equals(pet.getShelter().getId())) {
      throw new IllegalStateException("Only staff of the pet's shelter can remove this pet");
    }

    adoptionRepository.deleteByPet_Id(petId);
    deletePetImageIfPresent(pet);
    petRepository.delete(pet);
    LOGGER.info("Pet '{}' (id={}) removed from shelter '{}'", pet.getName(), petId, userShelter.getName());
  }

  private void deletePetImageIfPresent(Pet pet) {
    if (pet.getImagePath() == null || pet.getImagePath().isBlank()) {
      return;
    }
    try {
      Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(pet.getImageFilename());
      if (Files.exists(path)) {
        Files.delete(path);
      }
    } catch (IOException e) {
      LOGGER.warn("Failed to delete pet image file: {}", e.getMessage());
    }
  }

  /**
   * Returns true if the current user is staff of the shelter that owns this pet and can remove it.
   */
  public boolean canRemovePet(Long petId, String username) {
    Pet pet = petRepository.findById(petId).orElse(null);
    if (pet == null) {
      return false;
    }
    return userRepository.findByUsername(username)
        .filter(u -> u.getShelter() != null && u.getShelter().getId().equals(pet.getShelter().getId()))
        .isPresent();
  }
}


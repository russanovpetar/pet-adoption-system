package pet.adoption.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.RegisterRequest;
import pet.adoption.system.dto.request.ShelterStaffRequest;
import pet.adoption.system.entity.Role;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.ShelterNotFoundException;
import pet.adoption.system.exception.ShelterStaffAlreadyExistsException;
import pet.adoption.system.exception.UserAlreadyExistsException;
import pet.adoption.system.repository.ShelterRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class UserService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepo;
  private final ShelterRepository shelterRepo;
  private final PasswordEncoder encoder;

  public UserService(UserRepository userRepo, ShelterRepository shelterRepo, PasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.shelterRepo = shelterRepo;
    this.encoder = encoder;
  }

  public void registerAdopter(RegisterRequest request) {
    if (userRepo.existsByUsername(request.getUsername())) {
      LOGGER.warn("Registration failed - username already exists.");
      throw new UserAlreadyExistsException();
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(encoder.encode(request.getPassword()));
    user.setRole(Role.ADOPTER);

    userRepo.save(user);
  }

  public void createShelterStaff(ShelterStaffRequest request) {
    if (userRepo.existsByUsername(request.getUsername())) {
      throw new ShelterStaffAlreadyExistsException();
    }

    Shelter shelter = shelterRepo.findById(request.getShelterId())
        .orElseThrow(ShelterNotFoundException::new);

    User staff = new User();
    staff.setUsername(request.getUsername());
    staff.setPassword(encoder.encode(request.getPassword()));
    staff.setRole(Role.SHELTER_STAFF);
    staff.setShelter(shelter);

    userRepo.save(staff);
  }
}

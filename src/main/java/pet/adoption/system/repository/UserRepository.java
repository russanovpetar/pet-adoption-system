package pet.adoption.system.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pet.adoption.system.entity.Role;
import pet.adoption.system.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByUsername(String username);
  Optional<User> findByUsername(String username);

  /** Users with ADOPTER role and no shelter assigned - eligible to become shelter staff */
  List<User> findByRoleAndShelterIsNullOrderByUsernameAsc(Role role);

  /** Staff members assigned to a shelter (SHELTER_STAFF with this shelter) */
  List<User> findByShelter_IdOrderByUsernameAsc(Long shelterId);
}

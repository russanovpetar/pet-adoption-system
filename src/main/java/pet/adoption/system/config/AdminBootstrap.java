package pet.adoption.system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pet.adoption.system.entity.Role;
import pet.adoption.system.entity.User;
import pet.adoption.system.repository.UserRepository;

@Component
public class AdminBootstrap implements CommandLineRunner {

  private final UserRepository userRepo;
  private final PasswordEncoder encoder;

  public AdminBootstrap(UserRepository userRepo, PasswordEncoder encoder) {
    this.userRepo = userRepo;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {
    if (!userRepo.existsByUsername("admin")) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(encoder.encode("admin123"));
      admin.setRole(Role.ADMIN);
      userRepo.save(admin);
    }
  }
}


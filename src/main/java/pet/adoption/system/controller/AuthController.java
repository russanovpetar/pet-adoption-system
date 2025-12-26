package pet.adoption.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.dto.request.RegisterRequest;
import pet.adoption.system.exception.UserAlreadyExistsException;
import pet.adoption.system.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      userService.registerAdopter(request);
      return ResponseEntity.ok(new GenericResponse("Registered successfully"));
    } catch (UserAlreadyExistsException ex) {
      return ResponseEntity.badRequest().body(new GenericResponse("Username already exists"));
    }
  }
}

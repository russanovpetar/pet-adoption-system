package pet.adoption.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.dto.request.ShelterStaffRequest;
import pet.adoption.system.exception.ShelterNotFoundException;
import pet.adoption.system.exception.ShelterStaffAlreadyExistsException;
import pet.adoption.system.service.UserService;

@RestController
@RequestMapping("/api/admin/staff")
@PreAuthorize("hasRole('ADMIN')")
public class ShelterStaffController {
  private final UserService userService;

  public ShelterStaffController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<?> createStaff(@RequestBody ShelterStaffRequest request) {
    try {
      userService.createShelterStaff(request);
      return ResponseEntity.ok(new GenericResponse("Shelter staff user created successfully"));
    } catch (ShelterStaffAlreadyExistsException ex) {
      return ResponseEntity.badRequest().body(new GenericResponse("Shelter staff user already exists"));
    } catch (ShelterNotFoundException snfe) {
      return ResponseEntity.badRequest().body(new GenericResponse("Shelter not found"));
    }
  }
}

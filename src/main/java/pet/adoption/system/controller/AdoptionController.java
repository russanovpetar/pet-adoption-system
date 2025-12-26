package pet.adoption.system.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pet.adoption.system.dto.request.AdoptionApplicationRequest;
import pet.adoption.system.dto.response.AdoptionApplicationResponse;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.entity.AdoptionApplication;
import pet.adoption.system.exception.AdoptionApplicationNotFoundException;
import pet.adoption.system.exception.PetNotFoundException;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.service.AdoptionService;

@RestController
@RequestMapping("/api/adoption")
public class AdoptionController {
  private final AdoptionService adoptionService;

  public AdoptionController(AdoptionService adoptionService) {
    this.adoptionService = adoptionService;
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> listAllApplications() {
    try {
      List<AdoptionApplicationResponse> applications = adoptionService.getAllApplications();
      return ResponseEntity.status(HttpStatus.OK).body(applications);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }

  @GetMapping("/my")
  @PreAuthorize("hasRole('ADOPTER')")
  public ResponseEntity<?> listApplicationsForAdopter(Authentication authentication) {
    try {
      List<AdoptionApplicationResponse> applications = adoptionService.getApplicationsForAdopter(authentication.getName());
      return ResponseEntity.status(HttpStatus.OK).body(applications);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }

  @PostMapping
  @PreAuthorize("hasRole('ADOPTER')")
  public ResponseEntity<?> apply(@Valid @RequestBody AdoptionApplicationRequest request, Authentication authentication) {
    try {
      AdoptionApplicationResponse response = adoptionService.applyForAdoption(request, authentication.getName());
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (PetNotFoundException pnfe) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Pet not found"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }

  @GetMapping("/shelter")
  @PreAuthorize("hasRole('SHELTER_STAFF')")
  public ResponseEntity<?> listApplicationsForShelter(Authentication authentication) {
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(adoptionService.getApplicationsForShelter(authentication.getName()));
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }

  @PostMapping("/{appId}/review")
  @PreAuthorize("hasRole('SHELTER_STAFF')")
  public ResponseEntity<?> review(
      @PathVariable Long appId,
      @RequestParam("status") AdoptionApplication.Status status,
      Authentication authentication) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(adoptionService.reviewApplication(appId, status, authentication.getName()));
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (AdoptionApplicationNotFoundException aanfe) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Adoption application not found"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }
}

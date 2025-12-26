package pet.adoption.system.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pet.adoption.system.dto.request.PetRequest;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.entity.Pet;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.service.PetService;

@RestController
@RequestMapping("/api/pet")
public class PetController {
  private final PetService petService;

  public PetController(PetService petService) {
    this.petService = petService;
  }

  @GetMapping
  public ResponseEntity<?> browsePets() {
      return ResponseEntity.status(HttpStatus.OK).body(petService.browsePets());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> viewPet(@PathVariable Long id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(petService.viewPet(id));
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse("Pet not found"));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }

  @PostMapping
  @PreAuthorize("hasRole('SHELTER_STAFF')")
  public ResponseEntity<?> addPet(@Valid @RequestBody PetRequest request, Authentication authentication) {
    try {
      Pet pet = petService.addPet(request, authentication.getName());
      return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse("An error occurred"));
    }
  }
}

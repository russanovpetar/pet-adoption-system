package pet.adoption.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.dto.request.ShelterRequest;
import pet.adoption.system.exception.ShelterAlreadyExistsException;
import pet.adoption.system.exception.ShelterHasPetsException;
import pet.adoption.system.exception.ShelterNotFoundException;
import pet.adoption.system.service.ShelterService;

@RestController
@RequestMapping("/api/admin/shelter")
@PreAuthorize("hasRole('ADMIN')")
public class ShelterController {
  private final ShelterService shelterService;

  public ShelterController(ShelterService shelterService) {
    this.shelterService = shelterService;
  }

  @PostMapping
  public ResponseEntity<?> createShelter(@RequestBody ShelterRequest shelterRequest) {
    try {
      shelterService.createShelter(shelterRequest);
      return ResponseEntity.ok(new GenericResponse("Shelter created successfully"));
    } catch (ShelterAlreadyExistsException ex) {
      return ResponseEntity.badRequest().body(new GenericResponse("Shelter already exists"));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteShelter(@PathVariable Long id) {
    try {
      shelterService.deleteShelter(id);
      return ResponseEntity.ok(new GenericResponse("Shelter deleted successfully"));
    } catch (ShelterNotFoundException ex) {
      return ResponseEntity.notFound().build();
    } catch (ShelterHasPetsException ex) {
      return ResponseEntity.badRequest().body(new GenericResponse(ex.getMessage()));
    }
  }
}

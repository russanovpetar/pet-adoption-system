package pet.adoption.system.ui;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.dto.request.AdoptionApplicationRequest;
import pet.adoption.system.dto.request.PetRequest;
import pet.adoption.system.entity.Pet;
import pet.adoption.system.service.PetService;

@Controller
@RequestMapping("/ui/pet")
public class PetUIController {
  private final PetService petService;

  public PetUIController(PetService petService) {
    this.petService = petService;
  }

  @GetMapping
  public String listPets(Model model) {
    model.addAttribute("pets", petService.browsePets());
    return "pets/list";
  }

  @GetMapping("/{id}")
  public String petDetails(@PathVariable Long id, Model model) {
    Pet pet = petService.viewPet(id);
    model.addAttribute("pet", pet);
    AdoptionApplicationRequest form = new AdoptionApplicationRequest();
    form.setPetId(pet.getId());
    model.addAttribute("adoptionApplicationRequest", form);
    return "pets/details";
  }

  @GetMapping("/add")
  @PreAuthorize("isAuthenticated()")
  public String addPetForm(Model model) {
    model.addAttribute("petRequest", new PetRequest());
    return "pets/add";
  }

  @PostMapping("/add")
  @PreAuthorize("isAuthenticated()")
  public String addPet(@Valid @ModelAttribute PetRequest petRequest, BindingResult bindingResult, Authentication authentication) {
    if (bindingResult.hasErrors()) {
      return "pets/add";
    }
    petService.addPet(petRequest, authentication.getName());
    return "redirect:/ui/pet";
  }
}

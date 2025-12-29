package pet.adoption.system.ui;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.dto.request.ShelterRequest;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.service.ShelterService;

@Controller
@RequestMapping("/ui/admin/shelter")
public class ShelterUIController {
  private final ShelterService shelterService;

  public ShelterUIController(ShelterService shelterService) {
    this.shelterService = shelterService;
  }

  @GetMapping
  public String listShelters(Model model) {
    model.addAttribute("shelters", shelterService.listShelters());
    return "shelters/list";
  }

  @GetMapping("{id}")
  public String viewShelterDetails(@PathVariable Long id, Model model) {
    Shelter shelter = shelterService.getShelterById(id);
    model.addAttribute("shelter", shelter);
    return "shelters/details";
  }

  @GetMapping("/create")
  public String createShelterForm(Model model) {
    model.addAttribute("shelterRequest", new ShelterRequest());
    return "shelters/create";
  }

  @PostMapping("/create")
  public String createShelter(@Valid @ModelAttribute ShelterRequest shelterRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "shelters/create";
    }
    shelterService.createShelter(shelterRequest);
    return "redirect:/ui/admin/shelter";
  }
}

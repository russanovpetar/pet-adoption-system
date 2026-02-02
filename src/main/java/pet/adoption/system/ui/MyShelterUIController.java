package pet.adoption.system.ui;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.entity.Shelter;
import pet.adoption.system.service.PetService;
import pet.adoption.system.service.UserService;

@Controller
@RequestMapping("/ui/shelter")
public class MyShelterUIController {
  private final UserService userService;
  private final PetService petService;

  public MyShelterUIController(UserService userService, PetService petService) {
    this.userService = userService;
    this.petService = petService;
  }

  @GetMapping("/my")
  @PreAuthorize("hasRole('SHELTER_STAFF')")
  public String myShelter(Authentication authentication, Model model) {
    Shelter shelter = userService.getShelterForUsername(authentication.getName())
        .orElse(null);
    model.addAttribute("shelter", shelter);
    if (shelter != null) {
      model.addAttribute("pets", petService.getPetsByShelterId(shelter.getId()));
      model.addAttribute("staff", userService.getStaffByShelterId(shelter.getId()));
    } else {
      model.addAttribute("pets", java.util.Collections.emptyList());
      model.addAttribute("staff", java.util.Collections.emptyList());
    }
    return "shelters/my-shelter";
  }
}

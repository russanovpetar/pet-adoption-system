package pet.adoption.system.ui;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.dto.request.AdoptionApplicationRequest;
import pet.adoption.system.dto.response.AdoptionApplicationResponse;
import pet.adoption.system.service.AdoptionService;

@Controller
@RequestMapping("/ui/adoption")
public class AdoptionUIController {
  private final AdoptionService adoptionService;

  public AdoptionUIController(AdoptionService adoptionService) {
    this.adoptionService = adoptionService;
  }

  @GetMapping("/admin")
  public String listAllApplications(Model model) {
    model.addAttribute("allAdoptionApplications", adoptionService.getAllApplications());
    return "adoptions/admin-list";
  }

  @GetMapping("/my")
  public String listApplicationsForAdopter(Authentication authentication, Model model) {
    model.addAttribute("myAdoptionApplications", adoptionService.getApplicationsForAdopter(authentication.getName()));
    return "adoptions/my-list";
  }


  @PostMapping
  public String apply(@Valid @ModelAttribute AdoptionApplicationRequest request, Authentication authentication, Model model) {
    AdoptionApplicationResponse response = adoptionService.applyForAdoption(request, authentication.getName());
    model.addAttribute("adoptionApplication", response);
    return "adoptions/success";
  }
}

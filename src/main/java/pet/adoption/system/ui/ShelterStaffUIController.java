package pet.adoption.system.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.dto.request.ShelterStaffRequest;
import pet.adoption.system.service.UserService;

@Controller
@RequestMapping("/ui/admin/staff")
public class ShelterStaffUIController {
  private final UserService userService;

  public ShelterStaffUIController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/add/{shelterId}")
  public String addShelterStaffForm(@PathVariable Long shelterId, Model model) {
    ShelterStaffRequest request = new ShelterStaffRequest();
    request.setShelterId(shelterId);
    model.addAttribute("shelterStaffRequest", request);
    return "staff/add";
  }

  @PostMapping("/add")
  public String addShelterStaff(ShelterStaffRequest shelterStaffRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "staff/add";
    }
    userService.createShelterStaff(shelterStaffRequest);
    return String.format("redirect:/ui/admin/shelter/%s", shelterStaffRequest.getShelterId());
  }
}

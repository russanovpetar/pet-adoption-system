package pet.adoption.system.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.adoption.system.exception.ShelterStaffAlreadyExistsException;
import pet.adoption.system.service.ShelterService;
import pet.adoption.system.service.UserService;

@Controller
@RequestMapping("/ui/admin/staff")
public class ShelterStaffUIController {
  private final UserService userService;
  private final ShelterService shelterService;

  public ShelterStaffUIController(UserService userService, ShelterService shelterService) {
    this.userService = userService;
    this.shelterService = shelterService;
  }

  @GetMapping("/add/{shelterId}")
  public String addShelterStaffForm(@PathVariable Long shelterId, Model model) {
    var shelter = shelterService.getShelterById(shelterId);
    var eligibleUsers = userService.findEligibleUsersForShelterStaff();

    model.addAttribute("shelter", shelter);
    model.addAttribute("eligibleUsers", eligibleUsers);
    return "staff/add";
  }

  @PostMapping("/add")
  public String addShelterStaff(@RequestParam Long userId, @RequestParam Long shelterId,
      RedirectAttributes redirectAttributes) {
    try {
      userService.assignUserAsShelterStaff(userId, shelterId);
      redirectAttributes.addFlashAttribute("message", "User added as shelter staff.");
    } catch (ShelterStaffAlreadyExistsException e) {
      redirectAttributes.addFlashAttribute("error", "User is already assigned to a shelter.");
    }
    return "redirect:/ui/admin/shelter/" + shelterId;
  }
}

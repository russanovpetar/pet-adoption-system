package pet.adoption.system.ui;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pet.adoption.system.dto.request.RegisterRequest;
import pet.adoption.system.service.UserService;

@Controller
@RequestMapping("/ui/auth")
public class AuthUIController {
  private final UserService userService;

  public AuthUIController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("registerRequest", new RegisterRequest());
    return "auth/register";
  }

  @PostMapping("/register")
  public String register(
      @Valid @ModelAttribute RegisterRequest request,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return "auth/register";
    }

    userService.registerAdopter(request);

    return "redirect:/ui/auth/login?registered";
  }
}

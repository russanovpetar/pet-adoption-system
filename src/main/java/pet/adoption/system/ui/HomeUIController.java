package pet.adoption.system.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeUIController {

  @GetMapping({ "/", "/ui", "/ui/", "/ui/home" })
  public String home() {
    return "home";
  }
}

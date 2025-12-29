package pet.adoption.system.ui;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pet.adoption.system.dto.request.PostRequest;
import pet.adoption.system.dto.response.PostResponse;
import pet.adoption.system.service.PostRatingService;
import pet.adoption.system.service.PostService;

@Controller
@RequestMapping("/ui/post")
public class PostUIController {
  private final PostService postService;
  private final PostRatingService postRatingService;

  public PostUIController(PostService postService, PostRatingService postRatingService) {
    this.postService = postService;
    this.postRatingService = postRatingService;
  }

  @GetMapping
  public String browsePosts(Model model) {
    model.addAttribute("posts", postService.browsePosts());
    return "posts/list";
  }

  @GetMapping("/create")
  @PreAuthorize("isAuthenticated()")
  public String createPostForm(Model model) {
    model.addAttribute("postRequest", new PostRequest());
    return "posts/create";
  }

  @PostMapping("/create")
  @PreAuthorize("isAuthenticated()")
  public String createPost(@Valid @ModelAttribute PostRequest postRequest,
      BindingResult bindingResult,
      Authentication authentication) {
    if (bindingResult.hasErrors()) {
      return "posts/create";
    }
    postService.createPost(postRequest, authentication.getName());
    return "redirect:/ui/post";
  }

  @PostMapping("/{id}/rate")
  @PreAuthorize("isAuthenticated()")
  public String ratePost(@PathVariable Long id,
      @RequestParam int rating,
      Authentication authentication) {
    postRatingService.ratePost(id, rating, authentication.getName());
    return "redirect:/ui/post";
  }
}

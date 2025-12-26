package pet.adoption.system.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pet.adoption.system.dto.request.PostRatingRequest;
import pet.adoption.system.dto.request.PostRequest;
import pet.adoption.system.dto.response.GenericResponse;
import pet.adoption.system.dto.response.PostResponse;
import pet.adoption.system.exception.PostNotFoundException;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.service.PostRatingService;
import pet.adoption.system.service.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {
  private final PostService postService;
  private final PostRatingService postRatingService;

  public PostController(PostService postService, PostRatingService postRatingService) {
    this.postService = postService;
    this.postRatingService = postRatingService;
  }

  @GetMapping
  public List<PostResponse> browsePosts() {
    return postService.browsePosts();
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','ADOPTER','SHELTER_STAFF')")
  public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request,
      Authentication authentication) {
    try {
      String username = authentication.getName();
      PostResponse postResponse = postService.createPost(request, username);
      return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    }
  }

  @PostMapping("/{postId}/rating")
  @PreAuthorize("hasAnyRole('ADMIN','ADOPTER','SHELTER_STAFF')")
  public ResponseEntity<?> ratePost(@PathVariable Long postId,
      @Valid @RequestBody PostRatingRequest request,
      Authentication authentication) {
    try {
      postRatingService.ratePost(
          postId,
          request.getRating(),
          authentication.getName());
      return ResponseEntity.ok(new GenericResponse("Post rated successfully"));
    } catch (UserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("User not found"));
    } catch (PostNotFoundException pnfe) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Post not found"));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Error rating post"));
    }
  }
}
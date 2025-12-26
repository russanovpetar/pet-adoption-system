package pet.adoption.system.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.adoption.system.entity.Post;
import pet.adoption.system.entity.PostRating;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.PostNotFoundException;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.repository.PostRatingRepository;
import pet.adoption.system.repository.PostRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class PostRatingService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(PostRatingService.class);

  private final PostRepository postRepository;
  private final PostRatingRepository ratingRepository;
  private final UserRepository userRepository;

  public PostRatingService(PostRepository postRepository,
      PostRatingRepository ratingRepository,
      UserRepository userRepository) {
    this.postRepository = postRepository;
    this.ratingRepository = ratingRepository;
    this.userRepository = userRepository;
  }

  public void ratePost(Long postId, int rating, String username) {
    User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    Optional<PostRating> existingPostRating = ratingRepository.findByPostAndUser(post, user);

    PostRating postRating = existingPostRating.orElseGet(PostRating::new);
    postRating.setPost(post);
    postRating.setUser(user);
    postRating.setRating(rating);

    ratingRepository.save(postRating);

    LOGGER.info("Rated post {} with {}", postId, rating);
  }
}

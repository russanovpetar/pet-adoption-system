package pet.adoption.system.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.adoption.system.dto.request.PostRequest;
import pet.adoption.system.dto.response.PostResponse;
import pet.adoption.system.entity.Post;
import pet.adoption.system.entity.User;
import pet.adoption.system.exception.UserNotFoundException;
import pet.adoption.system.repository.PostRatingRepository;
import pet.adoption.system.repository.PostRepository;
import pet.adoption.system.repository.UserRepository;

@Service
public class PostService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ShelterService.class);
  private final PostRepository postRepository;
  private final PostRatingRepository postRatingRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, PostRatingRepository postRatingRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.postRatingRepository = postRatingRepository;
    this.userRepository = userRepository;
  }

  public List<PostResponse> browsePosts() {
    return postRepository.findAll().stream().map(this::toResponse).toList();
  }

  public PostResponse createPost(PostRequest request, String username) {
    User author = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    Post post = new Post();
    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setAuthor(author);

    LOGGER.info("Post created");

    return toResponse(postRepository.save(post));
  }

  private PostResponse toResponse(Post post) {
    Double avg = postRatingRepository.findAverageRatingByPostId(post.getId());
    Long count = postRatingRepository.countByPostId(post.getId());

    return new PostResponse(post, avg == null ? 0.0 : avg, count);
  }
}

package pet.adoption.system.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pet.adoption.system.entity.Post;
import pet.adoption.system.entity.PostRating;
import pet.adoption.system.entity.User;

public interface PostRatingRepository extends JpaRepository<PostRating, Long> {
  Optional<PostRating> findByPostAndUser(Post post, User user);

  List<PostRating> findByPost(Post post);

  @Query("SELECT AVG(r.rating) FROM PostRating r WHERE r.post.id = :postId")
  Double findAverageRatingByPostId(@Param("postId") Long postId);

  Long countByPostId(Long postId);
}

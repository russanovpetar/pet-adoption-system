package pet.adoption.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
public class PostRating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Post post;

  @ManyToOne(optional = false)
  private User user;

  @Min(1)
  @Max(5)
  private int rating;

  private LocalDateTime ratedAt;

  @PrePersist
  void onRate() {
    this.ratedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public LocalDateTime getRatedAt() {
    return ratedAt;
  }

  public void setRatedAt(LocalDateTime ratedAt) {
    this.ratedAt = ratedAt;
  }
}

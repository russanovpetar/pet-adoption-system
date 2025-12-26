package pet.adoption.system.dto.response;

import java.time.LocalDateTime;
import pet.adoption.system.entity.Post;

public class PostResponse {
  private final Long id;
  private final String title;
  private final String content;
  private final String authorUsername;
  private final Double averageRating;
  private final Long ratingCount;
  private final LocalDateTime createdAt;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.authorUsername = post.getAuthor().getUsername();
    this.createdAt = post.getCreatedAt();
    this.averageRating = 0.0;
    this.ratingCount = 0L;
  }

  public PostResponse(Post post, Double averageRating, Long ratingCount) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.authorUsername = post.getAuthor().getUsername();
    this.createdAt = post.getCreatedAt();
    this.averageRating = averageRating;
    this.ratingCount = ratingCount;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getAuthorUsername() {
    return authorUsername;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Double getAverageRating() {
    return averageRating;
  }

  public Long getRatingCount() {
    return ratingCount;
  }
}

package pet.adoption.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.adoption.system.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {}

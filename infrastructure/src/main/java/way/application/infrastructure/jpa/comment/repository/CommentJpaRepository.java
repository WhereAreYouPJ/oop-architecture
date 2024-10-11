package way.application.infrastructure.jpa.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import way.application.infrastructure.jpa.comment.entity.CommentEntity;
@Repository
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
}

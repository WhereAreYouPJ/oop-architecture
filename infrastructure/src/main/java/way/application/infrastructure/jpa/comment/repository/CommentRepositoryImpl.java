package way.application.infrastructure.jpa.comment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.jpa.comment.entity.CommentEntity;

@Component
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentJpaRepository.save(comment);
    }
}

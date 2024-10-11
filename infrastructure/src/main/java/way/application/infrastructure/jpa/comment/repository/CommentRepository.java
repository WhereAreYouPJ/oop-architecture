package way.application.infrastructure.jpa.comment.repository;

import way.application.infrastructure.jpa.comment.entity.CommentEntity;

public interface CommentRepository {

    CommentEntity saveComment(CommentEntity comment);
}

package way.application.service.comment.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import way.application.infrastructure.jpa.comment.entity.CommentEntity;

import static way.application.service.member.dto.request.MemberRequestDto.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "commentSeq", ignore = true)
    CommentEntity toCommentEntity(DeleteMemberDto request);

}

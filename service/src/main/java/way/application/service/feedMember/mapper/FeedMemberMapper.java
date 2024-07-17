package way.application.service.feedMember.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedMember.entity.FeedMemberEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMemberMapper {
	@Mapping(target = "feedMemberSeq", ignore = true)
	FeedMemberEntity toFeedMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

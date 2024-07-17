package way.application.service.feed.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {
	@Mapping(target = "feedSeq", ignore = true)
	@Mapping(target = "schedule", source = "schedule")
	@Mapping(target = "creatorMember", source = "creatorMember")
	@Mapping(target = "title", source = "title")
	@Mapping(target = "content", source = "content")
	FeedEntity toFeedEntity(ScheduleEntity schedule, MemberEntity creatorMember, String title, String content);
}

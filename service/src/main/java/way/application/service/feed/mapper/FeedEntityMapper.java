package way.application.service.feed.mapper;

import static way.application.service.feed.dto.response.FeedResponseDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedEntityMapper {
	@Mapping(target = "feedSeq", ignore = true)
	@Mapping(target = "schedule", source = "schedule")
	@Mapping(target = "creatorMember", source = "creatorMember")
	@Mapping(target = "title", source = "title")
	@Mapping(target = "content", source = "content")
	FeedEntity toFeedEntity(ScheduleEntity schedule, MemberEntity creatorMember, String title, String content);

	SaveFeedResponseDto toSaveFeedResponseDto(FeedEntity feedEntity);
}

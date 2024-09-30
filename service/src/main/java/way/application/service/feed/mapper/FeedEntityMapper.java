package way.application.service.feed.mapper;

import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.GetFeedResponseDto.*;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
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

	@Mapping(source = "scheduleEntity.scheduleSeq", target = "scheduleSeq")
	@Mapping(source = "scheduleEntity.startTime", target = "startTime")
	@Mapping(source = "scheduleEntity.location", target = "location")
	ScheduleInfo toScheduleInfo(ScheduleEntity scheduleEntity);

	@Mapping(source = "feedEntity.feedSeq", target = "feedSeq")
	@Mapping(source = "feedEntity.title", target = "title")
	@Mapping(source = "feedEntity.content", target = "content")
	FeedInfo toFeedInfo(FeedEntity feedEntity);

	@Mapping(source = "creatorMember.memberSeq", target = "memberSeq")
	@Mapping(source = "creatorMember.userName", target = "userName")
	@Mapping(source = "creatorMember.profileImage", target = "profileImage")
	MemberInfo toMemberInfo(MemberEntity creatorMember);

	List<FeedImageInfo> toFeedImageInfos(List<FeedImageEntity> feedImageEntities);

	default ScheduleFeedInfo toScheduleFeedInfo(
		MemberEntity creatorMember,
		FeedEntity feedEntity,
		List<FeedImageEntity> feedImageEntities,
		boolean bookMarkInfo
	) {
		MemberInfo memberInfo = toMemberInfo(creatorMember);
		FeedInfo feedInfo = toFeedInfo(feedEntity);
		List<FeedImageInfo> feedImageInfos = toFeedImageInfos(feedImageEntities);

		return new ScheduleFeedInfo(memberInfo, feedInfo, feedImageInfos, bookMarkInfo);
	}

	GetFeedResponseDto toGetFeedResponseDto(ScheduleInfo scheduleInfo, List<ScheduleFeedInfo> scheduleFeedInfo);
}

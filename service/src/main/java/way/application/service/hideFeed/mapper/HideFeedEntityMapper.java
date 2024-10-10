package way.application.service.hideFeed.mapper;

import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HideFeedEntityMapper {
	@Mapping(target = "hideFeedSeq", ignore = true)
	@Mapping(target = "feedEntity", source = "feedEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	HideFeedEntity toHideFeedEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	AddHideFeedResponseDto toAddHideFeedResponseDto(HideFeedEntity hideFeedEntity);

	@Mapping(target = "profileImage", source = "hideFeedEntity.memberEntity.profileImage")
	@Mapping(target = "startTime", source = "hideFeedEntity.feedEntity.schedule.startTime")
	@Mapping(target = "location", source = "hideFeedEntity.feedEntity.schedule.location")
	@Mapping(target = "title", source = "hideFeedEntity.feedEntity.title")
	@Mapping(target = "content", source = "hideFeedEntity.feedEntity.content")
	@Mapping(target = "bookMark", source = "bookMark")
	GetHideFeedResponseDto toGetHideFeedResponseDto(
		HideFeedEntity hideFeedEntity,
		List<hideFeedImageInfo> hideFeedImageInfos,
		Boolean bookMark
	);

	hideFeedImageInfo toHideFeedImageInfo(FeedImageEntity feedImageEntity);
}
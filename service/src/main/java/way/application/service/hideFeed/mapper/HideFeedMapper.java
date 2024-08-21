package way.application.service.hideFeed.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HideFeedMapper {
	@Mapping(target = "hideFeedSeq", ignore = true)
	@Mapping(target = "feedEntity", source = "feedEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	HideFeedEntity toHideFeedEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

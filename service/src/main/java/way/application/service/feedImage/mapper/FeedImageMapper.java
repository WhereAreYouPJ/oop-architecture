package way.application.service.feedImage.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedImageMapper {
	@Mapping(target = "feedImageSeq", ignore = true)
	@Mapping(target = "feedEntity", source = "feed")
	@Mapping(target = "feedImageURL", source = "feedImageURL")
	@Mapping(target = "feedImageOrder", source = "feedImageOrder")
	FeedImageEntity toFeedImageEntity(FeedEntity feed, String feedImageURL, Integer feedImageOrder);
}

package way.application.service.feedImage.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedImageMapper {
	@Mapping(target = "feedImageSeq", ignore = true)
	@Mapping(target = "feed", source = "feed")
	@Mapping(target = "feedImageURL", source = "feedImageURL")
	FeedImageEntity toFeedImageEntity(FeedEntity feed, String feedImageURL);
}

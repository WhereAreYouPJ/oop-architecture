package way.presentation.feed.mapper;

import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.presentation.feed.vo.response.FeedResponseVo.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedResponseMapper {
	SaveFeedResponse toSaveFeedResponse(SaveFeedResponseDto responseDto);
}

package way.presentation.hideFeed.mapper;

import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;
import static way.presentation.hideFeed.vo.response.HideFeedResponseVo.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HideFeedResponseMapper {
	AddHideFeedResponse toAddHideFeedResponse(AddHideFeedResponseDto responseDto);
}

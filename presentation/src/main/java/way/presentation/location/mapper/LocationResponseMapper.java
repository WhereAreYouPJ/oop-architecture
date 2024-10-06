package way.presentation.location.mapper;

import static way.application.service.location.dto.response.LocationResponseDto.*;
import static way.presentation.location.vo.res.LocationResponseVo.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationResponseMapper {
	AddLocationResponse toAddLocationResponse(AddLocationResponseDto responseDto);

	GetLocationResponse toGetLocationResponse(GetLocationResponseDto responseDto);
}

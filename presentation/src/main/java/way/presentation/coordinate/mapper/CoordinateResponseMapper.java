package way.presentation.coordinate.mapper;

import static way.application.service.coordinate.dto.response.CoordinateResponseDto.*;
import static way.presentation.coordinate.vo.response.CoordinateResponse.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoordinateResponseMapper {
	GetCoordinateResponse toGetCoordinateResponse(GetCoordinateResponseDto responseDto);
}

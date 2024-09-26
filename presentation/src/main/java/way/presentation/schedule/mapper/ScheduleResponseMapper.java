package way.presentation.schedule.mapper;

import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;
import static way.presentation.schedule.vo.response.ScheduleResponseVo.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleResponseMapper {
	SaveScheduleResponse toSaveScheduleResponse(SaveScheduleResponseDto responseDto);

	ModifyScheduleResponse toModifyScheduleResponse(ModifyScheduleResponseDto responseDto);
}

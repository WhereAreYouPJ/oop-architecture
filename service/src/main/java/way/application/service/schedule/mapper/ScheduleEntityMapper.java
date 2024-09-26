package way.application.service.schedule.mapper;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleEntityMapper {
	@Mapping(target = "scheduleSeq", ignore = true)
	ScheduleEntity toScheduleEntity(SaveScheduleRequestDto scheduleDto);

	GetScheduleResponseDto toGetScheduleResponseDto(ScheduleEntity scheduleEntity, List<String> userNames);

	GetScheduleByDateResponseDto toGetScheduleByDateResponseDto(ScheduleEntity scheduleEntity, Boolean isGroup);
}

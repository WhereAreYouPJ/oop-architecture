package way.application.service.schedule.mapper;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
	@Mapping(target = "scheduleSeq", ignore = true)
	ScheduleEntity toScheduleEntity(SaveScheduleRequestDto scheduleDto);
}

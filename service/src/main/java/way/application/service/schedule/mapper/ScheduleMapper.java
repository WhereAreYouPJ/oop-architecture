package way.application.service.schedule.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
	@Mapping(target = "scheduleSeq", ignore = true)
	ScheduleEntity toScheduleEntity(SaveScheduleRequestDto scheduleDto);
}

package way.application.service.schedule.mapper;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.GetScheduleResponseDto.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleEntityMapper {
	@Mapping(target = "scheduleSeq", ignore = true)
	ScheduleEntity toScheduleEntity(SaveScheduleRequestDto scheduleDto);

	GetScheduleResponseDto toGetScheduleResponseDto(
		ScheduleEntity scheduleEntity,
		List<GetScheduleMemberInfoDto> memberInfos
	);

	default List<GetScheduleMemberInfoDto> mapToGetScheduleMemberInfo(Map<Long, String> memberInfoMap) {
		return memberInfoMap.entrySet().stream()
			.map(entry -> new GetScheduleMemberInfoDto(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());
	}

	GetScheduleByDateResponseDto toGetScheduleByDateResponseDto(ScheduleEntity scheduleEntity, Boolean group);

	GetScheduleByMonthResponseDto toGetScheduleByMonthResponseDto(ScheduleEntity scheduleEntity);

	GetDdayScheduleResponseDto toGetDdayScheduleResponseDto(ScheduleEntity scheduleEntity, String dDay);

	GetScheduleListResponseDto toGetScheduleListResponseDto(ScheduleEntity scheduleEntity, Boolean feedExists);

	SaveScheduleResponseDto toSaveScheduleResponseDto(Long scheduleSeq, String chatRoomSeq);
}

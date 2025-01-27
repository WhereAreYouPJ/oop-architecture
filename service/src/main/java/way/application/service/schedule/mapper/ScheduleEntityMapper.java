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
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleEntityMapper {
	@Mapping(target = "scheduleSeq", ignore = true)
	ScheduleEntity toScheduleEntity(SaveScheduleRequestDto scheduleDto);

	GetScheduleResponseDto toGetScheduleResponseDto(
		ScheduleEntity scheduleEntity,
		List<GetScheduleMemberInfoDto> memberInfos
	);

	default List<GetScheduleMemberInfoDto> mapToGetScheduleMemberInfo(List<ScheduleMemberEntity> scheduleEntities) {
		return scheduleEntities.stream()
			.map(entry -> new GetScheduleMemberInfoDto(entry.getInvitedMember().getMemberSeq(), entry.getInvitedMember().getUserName(),entry.getIsCreator()))
			.collect(Collectors.toList());
	}

	GetScheduleByDateResponseDto toGetScheduleByDateResponseDto(ScheduleEntity scheduleEntity, Boolean group, Long creator);

	GetScheduleByMonthResponseDto toGetScheduleByMonthResponseDto(ScheduleEntity scheduleEntity);

	GetDdayScheduleResponseDto toGetDdayScheduleResponseDto(ScheduleEntity scheduleEntity, Long dDay);

	GetScheduleListResponseDto toGetScheduleListResponseDto(ScheduleEntity scheduleEntity, Boolean feedExists);

	SaveScheduleResponseDto toSaveScheduleResponseDto(Long scheduleSeq, String chatRoomSeq);

	@Mapping(target = "scheduleSeq", source = "scheduleEntity.scheduleSeq")
	@Mapping(target = "createdAt", source = "scheduleEntity.createdAt")
	@Mapping(target = "startTime", source = "scheduleEntity.startTime")
	@Mapping(target = "title", source = "scheduleEntity.scheduleSeq")
	@Mapping(target = "location", source = "scheduleEntity.location")
	@Mapping(target = "dDay", source = "dDay")
	GetInvitedScheduleListResponseDto toGetInvitedScheduleListResponseDto(ScheduleEntity scheduleEntity, Long dDay);
}

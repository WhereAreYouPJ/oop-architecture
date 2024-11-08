package way.application.service.coordinate.mapper;

import static way.application.service.coordinate.dto.request.CoordinateRequestDto.*;
import static way.application.service.coordinate.dto.response.CoordinateResponseDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoordinateEntityMapper {
	@Mapping(target = "coordinateSeq", ignore = true)
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "scheduleEntity", source = "scheduleEntity")
	@Mapping(target = "x", source = "requestDto.x")
	@Mapping(target = "y", source = "requestDto.y")
	CoordinateEntity toCoordinateEntity(
		MemberEntity memberEntity,
		ScheduleEntity scheduleEntity,
		CreateCoordinateRequestDto requestDto
	);

	@Mapping(target = "memberSeq", source = "memberEntity.memberSeq")
	@Mapping(target = "userName", source = "memberEntity.userName")
	@Mapping(target = "profileImage", source = "memberEntity.profileImage")
	@Mapping(target = "x", source = "coordinateEntity.x")
	@Mapping(target = "y", source = "coordinateEntity.y")
	GetCoordinateResponseDto toGetCoordinateResponseDto(MemberEntity memberEntity, CoordinateEntity coordinateEntity);
}

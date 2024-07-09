package way.application.service.scheduleMember.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMemberMapper {
	@Mapping(target = "scheduleMemberSeq", ignore = true)
	ScheduleMemberEntity toScheduleMemberEntity(
		ScheduleEntity schedule, MemberEntity invitedMember, Boolean isCreator, Boolean acceptSchedule
	);
}
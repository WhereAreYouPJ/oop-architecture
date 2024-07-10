package way.application.domain.scheduleMember;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Component
public class ScheduleMemberDomain {

	public ScheduleMemberEntity createScheduleMemberEntity(
		ScheduleEntity schedule,
		MemberEntity invitedMember,
		Boolean isCreator,
		Boolean acceptSchedule
	) {
		return ScheduleMemberEntity.builder()
			.schedule(schedule)
			.invitedMember(invitedMember)
			.isCreator(isCreator)
			.acceptSchedule(acceptSchedule)
			.build();
	}

	public List<String> extractUserNameFromScheduleMemberEntities(
		List<ScheduleMemberEntity> scheduleMemberEntities
	) {
		return scheduleMemberEntities.stream()
			.map(sm -> sm.getInvitedMember().getUserName())
			.collect(Collectors.toList());
	}
}

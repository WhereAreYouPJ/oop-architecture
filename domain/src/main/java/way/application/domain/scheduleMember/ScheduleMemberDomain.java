package way.application.domain.scheduleMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

@Component
@RequiredArgsConstructor
public class ScheduleMemberDomain {

	public Map<Long, String> extractGetScheduleMemberInfo(List<ScheduleMemberEntity> scheduleMemberEntities) {
		return scheduleMemberEntities.stream()
			.collect(Collectors.toMap(
				sm -> sm.getInvitedMember().getMemberSeq(), // key: memberSeq
				sm -> sm.getInvitedMember().getUserName()   // value: userName
			));
	}

	public List<String> extractUserNameFromList(List<ScheduleMemberEntity> scheduleMemberEntities) {
		return scheduleMemberEntities.stream()
			.map(entity -> entity.getInvitedMember().getUserName())
			.collect(Collectors.toList());
	}

	public List<ScheduleEntity> extractScheduleEntityList(List<ScheduleMemberEntity> scheduleMemberEntityList) {
		return scheduleMemberEntityList.stream()
			.map(ScheduleMemberEntity::getSchedule)
			.collect(Collectors.toList());
	}
}

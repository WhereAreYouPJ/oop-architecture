package way.application.domain.scheduleMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
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
}

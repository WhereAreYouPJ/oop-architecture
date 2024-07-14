package way.application.domain.scheduleMember;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Component
public class ScheduleMemberDomain {

	/**
	 * @param scheduleMemberEntities
	 * @return
	 *
	 * ScheduleMemberEntity 로부터 UserName 값 추출 메서드
	 */
	public List<String> extractUserNameFromScheduleMemberEntities(List<ScheduleMemberEntity> scheduleMemberEntities) {
		return scheduleMemberEntities.stream()
			.map(sm -> sm.getInvitedMember().getUserName())
			.collect(Collectors.toList());
	}
}

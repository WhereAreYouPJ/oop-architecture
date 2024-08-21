package way.application.domain.scheduleMember;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

@Component
@RequiredArgsConstructor
public class ScheduleMemberDomain {
	private final JPAQueryFactory queryFactory;

	public List<String> extractUserNameFromScheduleMemberEntities(List<ScheduleMemberEntity> scheduleMemberEntities) {
		return scheduleMemberEntities.stream()
			.map(sm -> sm.getInvitedMember().getUserName())
			.collect(Collectors.toList());
	}
}

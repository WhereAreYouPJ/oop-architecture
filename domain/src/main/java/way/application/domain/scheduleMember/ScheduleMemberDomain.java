package way.application.domain.scheduleMember;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.QScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.QScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Component
@RequiredArgsConstructor
public class ScheduleMemberDomain {
	private final JPAQueryFactory queryFactory;

	public List<String> extractUserNameFromScheduleMemberEntities(List<ScheduleMemberEntity> scheduleMemberEntities) {
		return scheduleMemberEntities.stream()
			.map(sm -> sm.getInvitedMember().getUserName())
			.collect(Collectors.toList());
	}

	public Page<ScheduleMemberEntity> findByMemberEntity(MemberEntity memberEntity, Pageable pageable) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;

		// QueryDSL을 사용하여 ScheduleMemberEntity를 조회
		QueryResults<ScheduleMemberEntity> results = queryFactory
			.selectFrom(scheduleMember)
			.join(scheduleMember.schedule, schedule).fetchJoin()
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<ScheduleMemberEntity> content = results.getResults();
		long total = results.getTotal();

		return new PageImpl<>(content, pageable, total);
	}
}

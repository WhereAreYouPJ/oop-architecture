package way.application.infrastructure.jpa.scheduleMember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.QScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ScheduleMemberRepositoryImpl implements ScheduleMemberRepository {
	private final ScheduleMemberJpaRepository scheduleMemberJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity) {
		return scheduleMemberJpaRepository.save(scheduleMemberEntity);
	}

	@Override
	public ScheduleEntity findScheduleIfCreatedByMember(Long scheduleSeq, Long memberSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.select(scheduleMember.schedule)
				.from(scheduleMember)
				.where(
					scheduleMember.schedule.scheduleSeq.eq(scheduleSeq)
						.and(scheduleMember.invitedMember.memberSeq.eq(memberSeq))
						.and(scheduleMember.acceptSchedule.isTrue())
						.and(scheduleMember.isCreator.isTrue())
				)
				.fetchOne())
			.orElseThrow(
				() -> new BadRequestException(ErrorResult.SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION)
			);
	}

	@Override
	public void deleteAllBySchedule(ScheduleEntity scheduleEntity) {
		scheduleMemberJpaRepository.deleteAllBySchedule(scheduleEntity);
	}

	@Override
	public ScheduleMemberEntity findAcceptedScheduleMemberInSchedule(Long scheduleSeq, Long memberSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.selectFrom(scheduleMember)
				.where(
					scheduleMember.schedule.scheduleSeq.eq(scheduleSeq)
						.and(scheduleMember.invitedMember.memberSeq.eq(memberSeq))
						.and(scheduleMember.acceptSchedule.isTrue())
				)
				.fetchOne())
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<ScheduleMemberEntity> findAllAcceptedScheduleMembersInSchedule(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.fetch();
	}

	@Override
	public ScheduleMemberEntity findScheduleMemberInSchedule(Long memberSeq, Long scheduleSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.selectFrom(scheduleMember)
				.where(
					scheduleMember.invitedMember.memberSeq.eq(memberSeq)
						.and(scheduleMember.schedule.scheduleSeq.eq(scheduleSeq))
				)
				.fetchOne())
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION));
	}

	@Override
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

	@Override
	public void deleteScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity) {
		scheduleMemberJpaRepository.delete(scheduleMemberEntity);
	}

	@Override
	public void deleteByScheduleEntity(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
			)
			.execute();
	}

	@Override
	public void deleteByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
					.and(scheduleMember.invitedMember.eq(memberEntity))
			)
			.execute();
	}
}

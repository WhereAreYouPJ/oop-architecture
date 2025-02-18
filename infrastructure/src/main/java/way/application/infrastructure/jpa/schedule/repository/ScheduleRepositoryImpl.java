package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import way.application.infrastructure.jpa.member.entity.QMemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.QScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
	private final ScheduleJpaRepository scheduleJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity) {
		return scheduleJpaRepository.save(scheduleEntity);
	}

	@Override
	public ScheduleEntity findByScheduleSeq(Long scheduleSeq) {
		return scheduleJpaRepository.findById(scheduleSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<ScheduleEntity> findAcceptedSchedulesByMemberAndDate(Long memberSeq, LocalDate date) {
		return scheduleJpaRepository.findAcceptedSchedulesByMemberAndDate(memberSeq, date);
	}

	@Override
	public List<ScheduleEntity> findSchedulesByYearMonth(
		LocalDateTime startOfMonth,
		LocalDateTime endOfMonth,
		Long memberSeq
	) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.select(schedule)
			.from(schedule)
			.join(scheduleMember).on(schedule.scheduleSeq.eq(scheduleMember.schedule.scheduleSeq))
			.where(
				(schedule.startTime.between(startOfMonth, endOfMonth)
					.or(schedule.endTime.between(startOfMonth, endOfMonth)))
					.and(scheduleMember.acceptSchedule.isTrue())
					.and(scheduleMember.invitedMember.memberSeq.eq(memberSeq))
			)
			.fetch();
	}

	@Override
	public void deleteScheduleEntity(ScheduleEntity scheduleEntity) {
		scheduleJpaRepository.delete(scheduleEntity);
	}

	@Override
	public List<ScheduleEntity> findSchedulesByMemberAndStartTime(MemberEntity memberEntity) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.select(schedule)
			.from(schedule)
			.join(scheduleMember).on(schedule.scheduleSeq.eq(scheduleMember.schedule.scheduleSeq))
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
					.and(schedule.startTime.goe(LocalDate.now().atStartOfDay()))
			)
			.orderBy(schedule.startTime.asc())
			.fetch();
	}

	@Override
	public Page<ScheduleEntity> findSchedulesByMemberEntity(MemberEntity memberEntity, Pageable pageable) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		QueryResults<ScheduleEntity> results = queryFactory
			.select(schedule)
			.from(schedule)
			.join(scheduleMember).on(schedule.scheduleSeq.eq(scheduleMember.schedule.scheduleSeq))
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.orderBy(schedule.startTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<ScheduleEntity> scheduleEntities = results.getResults();
		long total = results.getTotal();

		return new PageImpl<>(scheduleEntities, pageable, total);
	}

	@Override
	public Page<ScheduleEntity> findSchedulesByMemberEntityAndStartTime(
		MemberEntity memberEntity,
		LocalDateTime startTime,
		Pageable pageable
	) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;
		QMemberEntity member = QMemberEntity.memberEntity;

		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime currentDate = LocalDate.now().atStartOfDay();

		QueryResults<ScheduleEntity> results = queryFactory
				.select(schedule)
				.from(schedule)
				.join(scheduleMember).on(schedule.scheduleSeq.eq(scheduleMember.schedule.scheduleSeq))
				.join(member).on(scheduleMember.invitedMember.memberSeq.eq(member.memberSeq))
				.where(scheduleMember.acceptSchedule.isTrue()
						.and(schedule.endTime.before(currentTime) // 현재 시간 기준 endTime이 이전인 일정
								.or(schedule.allDay.eq(true) // 하루종일 일정
										.and(schedule.startTime.before(currentDate)))) // 현재 시간 이전
						.and(member.memberSeq.eq(memberEntity.getMemberSeq())))
				.orderBy(schedule.startTime.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	@Override
	public void deleteAllByMemberSeq(MemberEntity memberEntity, List<ScheduleEntity> scheduleEntities) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;

		queryFactory.delete(schedule)
			.where(schedule.in(scheduleEntities))
			.execute();

	}
}

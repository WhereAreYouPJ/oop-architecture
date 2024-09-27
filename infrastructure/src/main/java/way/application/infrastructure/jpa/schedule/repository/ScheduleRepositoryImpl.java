package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
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
	public void deleteById(Long scheduleSeq) {
		scheduleJpaRepository.deleteById(scheduleSeq);
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
	public Page<ScheduleEntity> getScheduleEntityFromScheduleMember(
		Page<ScheduleMemberEntity> scheduleMemberEntityPage
	) {
		// ScheduleMemberEntity에서 ScheduleEntity를 추출
		List<ScheduleEntity> scheduleEntities = scheduleMemberEntityPage
			.getContent()
			.stream()
			.map(ScheduleMemberEntity::getSchedule)
			.collect(Collectors.toList());

		// ScheduleEntity 리스트를 Page로 변환하여 반환
		return new PageImpl<>(
			scheduleEntities,
			scheduleMemberEntityPage.getPageable(),
			scheduleMemberEntityPage.getTotalElements()
		);
	}

	@Override
	public void deleteScheduleEntity(ScheduleEntity scheduleEntity) {
		scheduleJpaRepository.delete(scheduleEntity);
	}

	@Override
	public List<ScheduleEntity> findSchedulesByMember(MemberEntity memberEntity) {
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
	public Page<ScheduleEntity> findSchedulesByMemberEntityAndStartTime(
		MemberEntity memberEntity,
		LocalDateTime startTime,
		Pageable pageable
	) {
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;
		QMemberEntity member = QMemberEntity.memberEntity;

		QueryResults<ScheduleEntity> results = queryFactory
			.select(schedule)
			.from(schedule)
			.join(scheduleMember).on(schedule.scheduleSeq.eq(scheduleMember.schedule.scheduleSeq))
			.join(member).on(scheduleMember.invitedMember.memberSeq.eq(member.memberSeq))
			.where(
				scheduleMember.acceptSchedule.isTrue()
					.and(schedule.startTime.before(startTime))
					.and(member.memberSeq.eq(memberEntity.getMemberSeq()))
			)
			.orderBy(schedule.startTime.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}
}

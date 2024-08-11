package way.application.infrastructure.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.schedule.entity.QScheduleEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.QScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
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
}

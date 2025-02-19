package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface ScheduleRepository {
	ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity);

	ScheduleEntity findByScheduleSeq(Long scheduleSeq);

	List<ScheduleEntity> findAcceptedSchedulesByMemberAndDate(Long memberSeq, LocalDate date);

	List<ScheduleEntity> findSchedulesByYearMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Long memberSeq);

	void deleteScheduleEntity(ScheduleEntity scheduleEntity);

	List<ScheduleEntity> findSchedulesByMemberAndStartTime(MemberEntity memberEntity);

	Page<ScheduleEntity> findSchedulesByMemberEntity(MemberEntity memberEntity, Pageable pageable);

	Page<ScheduleEntity> findSchedulesByMemberEntityAndStartTime(
		MemberEntity memberEntity,
		LocalDateTime startTime,
		Pageable pageable
	);

	void deleteAllByMemberSeq(MemberEntity memberEntity,List<ScheduleEntity> scheduleEntities);
}

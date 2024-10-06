package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface ScheduleRepository {
	// Schedule Entity 저장
	ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity);

	// Schedule Seq 로 삭제
	void deleteById(Long scheduleSeq);

	// Schedule Seq 로 Validate 진행
	ScheduleEntity findByScheduleSeq(Long scheduleSeq);

	// memberSeq, Date, accept = true 조회
	List<ScheduleEntity> findAcceptedSchedulesByMemberAndDate(Long memberSeq, LocalDate date);

	// YearMonth 로 Schedule 조회
	List<ScheduleEntity> findSchedulesByYearMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Long memberSeq);

	void deleteScheduleEntity(ScheduleEntity scheduleEntity);

	List<ScheduleEntity> findSchedulesByMember(MemberEntity memberEntity);

	Page<ScheduleEntity> findSchedulesByMemberEntityAndStartTime(
		MemberEntity memberEntity,
		LocalDateTime startTime,
		Pageable pageable
	);
}

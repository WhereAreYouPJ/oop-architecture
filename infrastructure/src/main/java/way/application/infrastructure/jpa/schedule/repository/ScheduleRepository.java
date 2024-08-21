package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

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

	Page<ScheduleEntity> getScheduleEntityFromScheduleMember(Page<ScheduleMemberEntity> scheduleMemberEntityPage);

	void deleteScheduleEntity(ScheduleEntity scheduleEntity);
}

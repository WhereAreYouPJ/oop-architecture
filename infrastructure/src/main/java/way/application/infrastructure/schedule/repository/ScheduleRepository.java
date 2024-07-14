package way.application.infrastructure.schedule.repository;

import java.time.LocalDate;
import java.util.List;

import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface ScheduleRepository {
	// Schedule Entity 저장
	ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity);

	// Schedule Seq 로 삭제
	void deleteById(Long scheduleSeq);

	// Schedule Seq 로 Validate 진행
	ScheduleEntity findByScheduleSeq(Long scheduleSeq);

	// memberSeq, Date, accept = true 조회
	List<ScheduleEntity> findAcceptedSchedulesByMemberAndDate(Long memberSeq, LocalDate date);
}

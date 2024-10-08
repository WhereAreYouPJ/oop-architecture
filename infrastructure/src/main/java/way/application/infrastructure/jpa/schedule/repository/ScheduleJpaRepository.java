package way.application.infrastructure.jpa.schedule.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
	@Query("""
		SELECT 
		    se 
		FROM 
		    ScheduleEntity se 
		JOIN 
		    ScheduleMemberEntity sme 
		    ON 
		    se.scheduleSeq = sme.schedule.scheduleSeq 
		WHERE 
		    sme.invitedMember.memberSeq = :memberSeq 
		    AND 
		    sme.acceptSchedule = true 
		    AND 
		    :requestDate BETWEEN CAST(se.startTime AS date) AND CAST(se.endTime AS date)
		""")
	List<ScheduleEntity> findAcceptedSchedulesByMemberAndDate(
		@Param("memberSeq") Long memberSeq,
		@Param("requestDate") LocalDate requestDate
	);

	@Query("SELECT s FROM ScheduleEntity s WHERE s.startTime >= CURRENT_DATE")
	List<ScheduleEntity> findSchedulesAfterToday();
}

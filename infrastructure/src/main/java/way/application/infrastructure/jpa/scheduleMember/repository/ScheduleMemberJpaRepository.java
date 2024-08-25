package way.application.infrastructure.jpa.scheduleMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

@Repository
public interface ScheduleMemberJpaRepository extends JpaRepository<ScheduleMemberEntity, Long> {

	void deleteAllBySchedule(ScheduleEntity scheduleEntity);

	long countBySchedule(ScheduleEntity scheduleEntity);

}

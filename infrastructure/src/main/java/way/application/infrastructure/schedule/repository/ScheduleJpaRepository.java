package way.application.infrastructure.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Repository
public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

}

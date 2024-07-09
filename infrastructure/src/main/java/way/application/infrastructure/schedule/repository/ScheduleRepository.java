package way.application.infrastructure.schedule.repository;

import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface ScheduleRepository {
	ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity);
}

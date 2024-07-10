package way.application.infrastructure.schedule.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Component
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
	private final ScheduleJpaRepository scheduleJpaRepository;

	@Override
	public ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity) {
		return scheduleJpaRepository.save(scheduleEntity);
	}

	@Override
	public void deleteById(Long scheduleSeq) {
		scheduleJpaRepository.deleteById(scheduleSeq);
	}
}

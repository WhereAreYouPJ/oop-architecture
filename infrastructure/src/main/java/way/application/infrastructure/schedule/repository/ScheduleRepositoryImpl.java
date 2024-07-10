package way.application.infrastructure.schedule.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

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

	@Override
	public ScheduleEntity validateScheduleSeq(Long scheduleSeq) {
		return scheduleJpaRepository.findById(scheduleSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION));
	}
}

package way.application.service.schedule.utils;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.service.schedule.dto.request.ScheduleRequestDto.SaveScheduleRequestDto;
import way.application.service.schedule.mapper.ScheduleEntityMapper;

@Component
@RequiredArgsConstructor
public class ScheduleUtils {
	private final ScheduleRepository scheduleRepository;

	private final ScheduleEntityMapper scheduleEntityMapper;

	public ScheduleEntity saveScheduleEntity(SaveScheduleRequestDto request) {
		return scheduleRepository.saveSchedule(scheduleEntityMapper.toScheduleEntity(request));
	}
}

package way.application.domain.schedule;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@Slf4j
public class ScheduleDomain {
	public LocalDateTime getStartOfMonth(YearMonth yearMonth) {
		return yearMonth.atDay(1).atStartOfDay();
	}

	public LocalDateTime getEndOfMonth(YearMonth yearMonth) {
		return yearMonth.atEndOfMonth().atTime(23, 59, 59);
	}

	public void validateScheduleStartTime(LocalDateTime startTime) {
		LocalDateTime now = LocalDateTime.now();

		log.info("LocalDateTime now = {}", now);

		LocalDateTime oneHourBefore = now.minusHours(1);
		LocalDateTime oneHourAfter = now.plusHours(1);

		if (startTime.isBefore(oneHourBefore) || startTime.isAfter(oneHourAfter)) {
			throw new BadRequestException(ErrorResult.START_TIME_BAD_REQUEST_EXCEPTION);
		}
	}

	public Long getDdaySchedule(LocalDateTime startTime) {

		LocalDateTime now = LocalDateTime.now();

		return ChronoUnit.DAYS.between(now.toLocalDate(), startTime.toLocalDate());
	}

	public Page<ScheduleEntity> getScheduleEntityFromScheduleMember(Page<ScheduleMemberEntity> scheduleMemberEntity) {
		List<ScheduleEntity> scheduleEntities = scheduleMemberEntity
			.getContent()
			.stream()
			.map(ScheduleMemberEntity::getSchedule)
			.collect(Collectors.toList());

		return new PageImpl<>(
			scheduleEntities,
			scheduleMemberEntity.getPageable(),
			scheduleMemberEntity.getTotalElements()
		);
	}
}

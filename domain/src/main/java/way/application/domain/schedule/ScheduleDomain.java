package way.application.domain.schedule;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class ScheduleDomain {
	public LocalDateTime getStartOfMonth(YearMonth yearMonth) {
		return yearMonth.atDay(1).atStartOfDay();
	}

	public LocalDateTime getEndOfMonth(YearMonth yearMonth) {
		return yearMonth.atEndOfMonth().atTime(23, 59, 59);
	}

	public void validateScheduleStartTime(LocalDateTime startTime) {
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime oneHourBefore = now.minusHours(1);
		LocalDateTime oneHourAfter = now.plusHours(1);

		if (startTime.isAfter(oneHourBefore) && startTime.isBefore(oneHourAfter)) {
			throw new BadRequestException(ErrorResult.START_TIME_BAD_REQUEST_EXCEPTION);
		}
	}

	public String getDdaySchedule(LocalDateTime startTime) {

		LocalDateTime now = LocalDateTime.now();

		long day = ChronoUnit.DAYS.between(now.toLocalDate(), startTime.toLocalDate());

		if (day == 0 ) {
			return "D-day";
		}

		return "D-"+day;
	}
}

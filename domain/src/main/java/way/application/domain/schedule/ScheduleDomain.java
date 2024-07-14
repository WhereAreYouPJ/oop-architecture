package way.application.domain.schedule;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.springframework.stereotype.Component;

@Component
public class ScheduleDomain {
	public LocalDateTime getStartOfMonth(YearMonth yearMonth) {
		return yearMonth.atDay(1).atStartOfDay();
	}

	public LocalDateTime getEndOfMonth(YearMonth yearMonth) {
		return yearMonth.atEndOfMonth().atTime(23, 59, 59);
	}
}

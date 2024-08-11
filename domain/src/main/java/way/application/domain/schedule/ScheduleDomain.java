package way.application.domain.schedule;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Component
public class ScheduleDomain {
	public LocalDateTime getStartOfMonth(YearMonth yearMonth) {
		return yearMonth.atDay(1).atStartOfDay();
	}

	public LocalDateTime getEndOfMonth(YearMonth yearMonth) {
		return yearMonth.atEndOfMonth().atTime(23, 59, 59);
	}
}

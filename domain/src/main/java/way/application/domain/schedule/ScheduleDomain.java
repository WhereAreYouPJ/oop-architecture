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

	public Page<ScheduleEntity> getScheduleEntityFromScheduleMember(
		Page<ScheduleMemberEntity> scheduleMemberEntityPage
	) {
		// ScheduleMemberEntity에서 ScheduleEntity를 추출
		List<ScheduleEntity> scheduleEntities = scheduleMemberEntityPage
			.getContent()
			.stream()
			.map(ScheduleMemberEntity::getSchedule)
			.collect(Collectors.toList());

		// ScheduleEntity 리스트를 Page로 변환하여 반환
		return new PageImpl<>(
			scheduleEntities,
			scheduleMemberEntityPage.getPageable(),
			scheduleMemberEntityPage.getTotalElements()
		);
	}
}

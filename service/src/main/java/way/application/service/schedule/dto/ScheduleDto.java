package way.application.service.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ScheduleDto(
	String title,

	LocalDateTime startTime,

	LocalDateTime endTime,

	String location,

	String streetName,

	Double x,

	Double y,

	String color,

	String memo,

	List<Long> invitedMemberSeqs,

	Long createMemberSeq
) {

}

package way.presentation.schedule.vo.res;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record GetScheduleResponse(
	String title,

	LocalDateTime startTime,

	LocalDateTime endTime,

	String location,

	String streetName,

	Double x,

	Double y,

	String color,

	String memo,

	List<String> userName
) implements Serializable {
	private static final long serialVersionUID = 1L;
}
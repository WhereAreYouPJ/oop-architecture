package way.presentation.schedule.vo.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ScheduleResponseVo {
	public record SaveScheduleResponse(
		Long scheduleSeq
	) {

	}

	public record ModifyScheduleResponse(
		Long scheduleSeq
	) {

	}

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

	public record GetScheduleByDateResponse(
		Long scheduleSeq,
		String title,
		String location,
		String color,
		LocalDateTime startTime,
		LocalDateTime endTime,
		boolean group,
		Boolean allDay
	) {

	}

	public record GetScheduleByMonthResponse(
		Long scheduleSeq,
		String title,
		LocalDateTime startTime,
		LocalDateTime endTime,
		String location,
		String streetName,
		Double x,
		Double y,
		String color,
		String memo,
		Boolean allDay
	) {

	}

	public record GetDdayScheduleResponse(
		@Schema(description = "조회한 Schedule Seq")
		Long scheduleSeq,
		@Schema(description = "Schedule 제목")
		String title,
		@Schema(description = "남은 일수")
		String dDay
	) {

	}
}

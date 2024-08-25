package way.application.service.schedule.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleResponseDto {
	public record SaveScheduleResponseDto(
		Long scheduleSeq
	) {

	}

	public record ModifyScheduleResponseDto(
		Long scheduleSeq
	) {

	}

	public record GetScheduleResponseDto(
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

	public record GetScheduleByDateResponseDto(
		Long scheduleSeq,

		String title,

		String location,

		String color,

		LocalDateTime startTime,

		LocalDateTime endTime,

		boolean group
	) {

	}

	public record GetScheduleByMonthResponseDto(
		Long scheduleSeq,
		String title,
		LocalDateTime startTime,
		LocalDateTime endTime,
		String location,
		String streetName,

		Double x,
		Double y,
		String color,
		String memo
	) {

	}
}

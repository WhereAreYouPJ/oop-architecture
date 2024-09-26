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
		@Schema(description = "조회한 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "Schedule 제목")
		String title,

		@Schema(description = "Schedule 시작 시간")
		LocalDateTime startTime,

		@Schema(description = "Schedule 끝나는 시간")
		LocalDateTime endTime,

		@Schema(description = "Schedule 장소")
		String location,

		@Schema(description = "Schedule 도로명 주소")
		String streetName,

		@Schema(description = "위치 X 좌표")
		Double x,

		@Schema(description = "위치 Y 좌표")
		Double y,

		@Schema(description = "Schedule 색")
		String color,

		@Schema(description = "Schedule 메모")
		String memo,

		@Schema(description = "하루 종일 여부")
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

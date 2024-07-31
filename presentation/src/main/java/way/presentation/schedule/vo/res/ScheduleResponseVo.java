package way.presentation.schedule.vo.res;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ScheduleResponseVo {
	public record SaveScheduleResponse(
		@Schema(description = "Schedule DB에 저장한 이후 반환되는 Seq 값")
		Long scheduleSeq
	) {

	}

	public record ModifyScheduleResponse(
		@Schema(description = "Schedule DB에 저장한 이후 반환되는 Seq 값")
		Long scheduleSeq
	) {

	}

	public record GetScheduleResponse(
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

		List<String> userName
	) implements Serializable {
		private static final long serialVersionUID = 1L;
	}

	public record GetScheduleByDateResponse(
		@Schema(description = "해당 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "Schedule 제목")
		String title,

		@Schema(description = "Schedule 장소")
		String location,

		@Schema(description = "Schedule 색")
		String color,

		@Schema(description = "Schedule 시작 시간")
		LocalDateTime startTime,

		@Schema(description = "Schedule 끝나는 시간")
		LocalDateTime endTime
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
		String memo
	) {

	}
}

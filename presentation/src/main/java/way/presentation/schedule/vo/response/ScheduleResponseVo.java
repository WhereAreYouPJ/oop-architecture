package way.presentation.schedule.vo.response;

import static way.application.service.schedule.dto.response.ScheduleResponseDto.GetScheduleResponseDto.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
		Boolean allDay,
		List<GetScheduleMemberInfoDto> memberInfos
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
		Long scheduleSeq,
		String title,
		Long dDay
	) {

	}

	public record GetScheduleListResponse(
		Long scheduleSeq,
		LocalDateTime startTime,
		String title,
		Boolean feedExists,
		String location
	) {

	}

	public record GetInvitedScheduleListResponse(
		Long scheduleSeq,
		LocalDateTime startTime,
		String title,
		String location,
		Long dDay
	) {

	}
}

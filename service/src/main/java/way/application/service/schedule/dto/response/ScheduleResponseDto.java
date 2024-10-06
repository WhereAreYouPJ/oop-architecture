package way.application.service.schedule.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleResponseDto {
	public record SaveScheduleResponseDto(
		Long scheduleSeq,
		String chatRoomSeq
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
		Boolean allDay,
		List<GetScheduleMemberInfoDto> memberInfos
	) implements Serializable {
		private static final long serialVersionUID = 1L;

		public record GetScheduleMemberInfoDto(
			Long memberSeq,
			String userName
		) {

		}
	}

	public record GetScheduleByDateResponseDto(
		Long scheduleSeq,
		String title,
		String location,
		String color,
		LocalDateTime startTime,
		LocalDateTime endTime,
		Boolean group,
		Boolean allDay
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
		String memo,
		Boolean allDay
	) {

	}

	public record GetDdayScheduleResponseDto(
		Long scheduleSeq,
		String title,
		Long dDay
	) {

	}

	public record GetScheduleListResponseDto(
		Long scheduleSeq,
		LocalDateTime startTime,
		String title,
		Boolean feedExists,
		String location
	) {

	}

	public record GetInvitedScheduleListResponseDto(
		Long scheduleSeq,
		LocalDateTime startTime,
		String title,
		String location,
		Long dDay
	) {

	}
}

package way.application.service.schedule.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleRequestDto {
	public record SaveScheduleRequestDto(
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

	public record ModifyScheduleRequestDto(
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

		List<Long> invitedMemberSeqs,

		Long createMemberSeq
	) {
		public SaveScheduleRequestDto toSaveScheduleRequestDto() {
			return new SaveScheduleRequestDto(
				this.title,
				this.startTime,
				this.endTime,
				this.location,
				this.streetName,
				this.x,
				this.y,
				this.color,
				this.memo,
				this.invitedMemberSeqs,
				this.createMemberSeq
			);
		}
	}

	public record DeleteScheduleRequestDto(
		Long scheduleSeq,
		Long creatorSeq
	) {
	}

}
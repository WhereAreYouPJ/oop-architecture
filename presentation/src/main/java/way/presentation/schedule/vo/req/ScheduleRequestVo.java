package way.presentation.schedule.vo.req;

import java.time.LocalDateTime;
import java.util.List;

import way.application.service.schedule.dto.request.DeleteScheduleRequestDto;
import way.application.service.schedule.dto.request.ModifyScheduleRequestDto;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;

public class ScheduleRequestVo {
	public record SaveScheduleRequest(
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

	public record ModifyScheduleRequest(
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
		public ModifyScheduleRequestDto toModifyScheduleRequestDto() {
			return new ModifyScheduleRequestDto(
				this.scheduleSeq,
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

	public record DeleteScheduleRequest(
		Long scheduleSeq,
		Long creatorSeq
	) {
		public DeleteScheduleRequestDto toDeleteScheduleRequestDto() {
			return new DeleteScheduleRequestDto(
				this.scheduleSeq,
				this.creatorSeq
			);
		}
	}
}

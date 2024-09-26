package way.presentation.schedule.vo.request;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

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
		Boolean allDay,
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
				this.allDay,
				this.invitedMemberSeqs,
				this.createMemberSeq
			);
		}

		public void saveScheduleRequestValidate() {
			if (this.title == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.startTime == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.endTime == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.createMemberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
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
		Boolean allDay,
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
				this.allDay,
				this.invitedMemberSeqs,
				this.createMemberSeq
			);
		}

		public void modifyScheduleRequestValidate() {
			if (scheduleSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (title == null || title.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (startTime == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (endTime == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (createMemberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}

	public record DeleteScheduleRequest(
		Long scheduleSeq,
		Long memberSeq
	) {
		public DeleteScheduleRequestDto toDeleteScheduleRequestDto() {
			return new DeleteScheduleRequestDto(
				this.scheduleSeq,
				this.memberSeq
			);
		}

		public void deleteScheduleRequestValidate() {
			if (scheduleSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}

	public record GetScheduleByDateRequest(
		Long memberSeq,
		LocalDate date
	) {
		public GetScheduleByDateRequestDto toGetScheduleByDateRequestDto() {
			return new GetScheduleByDateRequestDto(
				this.memberSeq,
				this.date
			);
		}
	}

	public record AcceptScheduleRequest(
		@Schema(description = "수락하고자 하는 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "수락하고자 하는 Member Seq")
		Long memberSeq
	) {
		public AcceptScheduleRequestDto toAcceptScheduleRequestDto() {
			return new AcceptScheduleRequestDto(
				this.scheduleSeq,
				this.memberSeq
			);
		}
	}

	public record GetScheduleByMonthRequest(
		YearMonth yearMonth,
		Long memberSeq
	) {
		public GetScheduleByMonthRequestDto toGetScheduleByMonthRequestDto() {
			return new GetScheduleByMonthRequestDto(
				this.yearMonth,
				this.memberSeq
			);
		}
	}

	public record GetDdaySchedule(
		Long memberSeq
	) {
		public GetDdayScheduleDto toGetDdayScheduleDto() {
			return new GetDdayScheduleDto(
				this.memberSeq
			);
		}
	}

	public record RefuseScheduleRequest(
		Long memberSeq,
		Long scheduleSeq
	) {
		public RefuseScheduleRequestDto toRefuseScheduleRequestDto() {
			return new RefuseScheduleRequestDto(
				this.memberSeq,
				this.scheduleSeq
			);
		}
	}
}

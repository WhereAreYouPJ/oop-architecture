package way.presentation.schedule.vo.request;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ScheduleRequestVo {
	public record SaveScheduleRequest(
		@NotNull
		String title,

		@NotNull
		LocalDateTime startTime,

		@NotNull
		LocalDateTime endTime,

		String location,

		String streetName,

		Double x,

		Double y,

		String color,

		String memo,

		Boolean allDay,

		List<Long> invitedMemberSeqs,

		@NotNull
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
	}

	public record ModifyScheduleRequest(
		@Schema(description = "수정하고자 하는 Schedule Seq")
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
		Boolean allDay,

		@Schema(description = "Schedule 초대 Member Seq (중복으로 넘겨도 괜찮도록 구현해놨습니다.)")
		List<Long> invitedMemberSeqs,

		@Schema(description = "Schedule 생성자 Member Seq")
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
	}

	public record DeleteScheduleRequest(
		@Schema(description = "삭제하고자 하는 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "Schedule 생성 Member Seq")
		Long memberSeq
	) {
		public DeleteScheduleRequestDto toDeleteScheduleRequestDto() {
			return new DeleteScheduleRequestDto(
				this.scheduleSeq,
				this.memberSeq
			);
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

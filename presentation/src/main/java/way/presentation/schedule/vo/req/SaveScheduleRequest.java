package way.presentation.schedule.vo.req;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;

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
	public SaveScheduleRequestDto toScheduleDto() {
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

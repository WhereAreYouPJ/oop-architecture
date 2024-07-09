package way.presentation.schedule.vo.req;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;

public record SaveScheduleRequest(
	@NotEmpty(message = "title 값을 입력해주세요.")
	String title,

	@NotNull(message = "startTime 값을 입력해주세요.")
	LocalDateTime startTime,

	@NotNull(message = "endTime 값을 입력해주세요.")
	LocalDateTime endTime,

	@NotEmpty(message = "location 값을 입력해주세요.")
	String location,

	@NotEmpty(message = "streetName 값을 입력해주세요.")
	String streetName,

	@NotNull(message = "x 값을 입력해주세요.")
	Double x,

	@NotNull(message = "y 값을 입력해주세요.")
	Double y,

	@NotEmpty(message = "color 값을 입력해주세요.")
	String color,

	@NotEmpty(message = "memo 값을 입력해주세요.")
	String memo,

	List<Long> invitedMemberSeqs,

	@NotNull(message = "createMemberSeq 값을 입력해주세요")
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

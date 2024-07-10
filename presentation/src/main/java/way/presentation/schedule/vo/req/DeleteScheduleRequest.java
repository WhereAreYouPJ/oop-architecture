package way.presentation.schedule.vo.req;

import way.application.service.schedule.dto.request.DeleteScheduleRequestDto;

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

package way.application.service.schedule.dto.request;

public record DeleteScheduleRequestDto(
	Long scheduleSeq,
	Long creatorSeq
) {
}

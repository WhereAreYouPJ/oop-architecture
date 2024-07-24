package way.application.service.hideFeed.dto.request;

public class HideFeedRequestDto {
	public record AddHideFeedRequestDto(
		Long scheduleSeq,
		Long memberSeq
	) {

	}

	public record DeleteHideFeedRequestDto(
		Long hideScheduleSeq,
		Long memberSeq
	) {

	}
}

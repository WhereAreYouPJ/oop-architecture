package way.application.service.hideFeed.dto.request;

public class HideFeedRequestDto {
	public record AddHideFeedRequestDto(
		Long feedSeq,
		Long memberSeq
	) {

	}

	public record DeleteHideFeedRequestDto(
		Long hideFeedSeq,
		Long memberSeq
	) {

	}
}

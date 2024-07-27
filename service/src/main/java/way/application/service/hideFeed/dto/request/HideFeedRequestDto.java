package way.application.service.hideFeed.dto.request;

public class HideFeedRequestDto {
	public record AddHideFeedRequestDto(
		Long hideFeedSeq,
		Long memberSeq
	) {

	}
}

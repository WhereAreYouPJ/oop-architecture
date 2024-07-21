package way.application.service.feed.dto.response;

public class FeedResponseDto {
	public record SaveFeedResponseDto(
		Long feedSeq
	) {

	}

	public record ModifyFeedResponseDto(
		Long feedSeq
	) {

	}
}

package way.application.service.feed.dto.request;

import java.util.List;

public class FeedRequestDto {
	public record SaveFeedRequestDto(
		Long scheduleSeq,
		Long memberSeq,
		String title,
		String content,
		List<Integer> feedImageOrders
	) {

	}

	public record ModifyFeedRequestDto(
		Long feedSeq,
		Long memberSeq,
		String title,
		String content,
		List<Integer> feedImageOrders
	) {
	}

	public record DeleteFeedRequestDto(
		Long memberSeq,
		Long feedSeq
	) {

	}
}

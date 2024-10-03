package way.application.service.feed.dto.request;

import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FeedRequestDto {
	public record SaveFeedRequestDto(
		Long scheduleSeq,
		Long memberSeq,
		String title,
		String content,
		List<feedImageInfo> feedImageInfos,
		List<feedImageOrder> feedImageOrders
	) {

	}

	public record ModifyFeedRequestDto(
		Long feedSeq,
		Long memberSeq,
		String title,
		String content,
		List<feedImageInfo> feedImageInfos,
		List<feedImageOrder> feedImageOrders
	) {
		public SaveFeedRequestDto toSaveFeedRequestDto(Long scheduleSeq) {
			return new SaveFeedRequestDto(
				scheduleSeq,
				this.memberSeq,
				this.title,
				this.content,
				this.feedImageInfos != null ? this.feedImageInfos : Collections.emptyList(),
				this.feedImageOrders != null ? this.feedImageOrders : Collections.emptyList()
			);
		}
	}

	public record feedImageInfo(
		MultipartFile images
	) {

	}

	public record feedImageOrder(
		Long feedImageOrder
	) {

	}
}

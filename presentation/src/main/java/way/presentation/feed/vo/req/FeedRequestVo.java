package way.presentation.feed.vo.req;

import static way.application.service.feed.dto.request.FeedRequestDto.*;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FeedRequestVo {
	public record SaveFeedRequest(
		Long scheduleSeq,
		Long creatorSeq,
		String title,
		String content,
		List<MultipartFile> images
	) {
		public SaveFeedRequestDto toSaveFeedRequest() {
			return new SaveFeedRequestDto(
				this.scheduleSeq,
				this.creatorSeq,
				this.title,
				this.content,
				this.images
			);
		}
	}

	public record ModifyReedRequest(
		Long feedSeq,
		Long creatorSeq,
		String title,
		String content,
		List<MultipartFile> images
	) {
		public ModifyFeedRequestDto toModifyFeedRequestDto() {
			return new ModifyFeedRequestDto(
				this.feedSeq,
				this.creatorSeq,
				this.title,
				this.content,
				this.images
			);
		}
	}

	public record HideFeedRequest(
		Long feedSeq,
		Long creatorSeq
	) {
		public HideFeedRequestDto toHideFeedRequestDto() {
			return new HideFeedRequestDto(
				this.feedSeq,
				this.creatorSeq
			);
		}
	}
}

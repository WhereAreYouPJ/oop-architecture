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
		List<ImageInfo> images
	) {

	}

	public record ModifyFeedRequestDto(
		Long feedSeq,
		Long memberSeq,
		String title,
		String content,
		List<ImageInfo> images
	) {
		public SaveFeedRequestDto toSaveFeedRequestDto(Long scheduleSeq) {
			return new SaveFeedRequestDto(
				scheduleSeq,
				this.memberSeq,
				this.title,
				this.content,
				this.images != null ? this.images : Collections.emptyList()
			);
		}
	}

	public record ImageInfo(
		MultipartFile images,
		Long order
	) {

	}
}

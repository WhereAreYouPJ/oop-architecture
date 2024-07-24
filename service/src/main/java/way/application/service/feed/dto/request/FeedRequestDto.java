package way.application.service.feed.dto.request;

import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FeedRequestDto {
	public record SaveFeedRequestDto(
		Long scheduleSeq,
		Long creatorSeq,
		String title,
		String content,
		List<MultipartFile> images
	) {

	}

	public record ModifyFeedRequestDto(
		Long feedSeq,
		Long creatorSeq,
		String title,
		String content,
		List<MultipartFile> images
	) {
		public SaveFeedRequestDto toSaveFeedRequestDto(Long scheduleSeq) {
			return new SaveFeedRequestDto(
				scheduleSeq,
				this.creatorSeq,
				this.title,
				this.content,
				this.images != null ? this.images : Collections.emptyList()
			);
		}
	}
}

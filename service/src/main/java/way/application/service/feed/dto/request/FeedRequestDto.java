package way.application.service.feed.dto.request;

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
}

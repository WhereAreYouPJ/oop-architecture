package way.application.service.hideFeed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class HideFeedResponseDto {
	public record AddHideFeedResponseDto(
		Long hideFeedSeq
	) {

	}

	public record GetHideFeedResponseDto(
		String profileImage,
		LocalDateTime startTime,
		String location,
		String title,
		List<String> feedImageUrl,
		String content

		// TODO: bookMark 기능 추가
		// Boolean bookMark
	) {

	}
}

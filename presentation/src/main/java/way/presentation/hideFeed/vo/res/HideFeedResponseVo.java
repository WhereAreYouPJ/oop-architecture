package way.presentation.hideFeed.vo.res;

import java.time.LocalDateTime;
import java.util.List;

public class HideFeedResponseVo {
	public record AddHideFeedResponse(
		Long hideFeedSeq
	) {

	}

	public record GetHideFeedResponse(
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

package way.application.service.hideFeed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class HideFeedResponseDto {

	public record GetHideFeedResponseDto(
		String profileImage,
		LocalDateTime startTime,
		String location,
		String title,
		List<hideFeedImageInfo> hideFeedImageInfos,
		String content,
		Boolean bookMark,
		List<hideFeedFriendInfo> feedFriendInfos
	) {

	}

	public record hideFeedImageInfo(
		Long feedImageSeq,
		String feedImageURL,
		Integer feedImageOrder
	) {

	}

	public record hideFeedFriendInfo(
		Long memberSeq,
		String userName,
		String profileImage
	) {

	}

}

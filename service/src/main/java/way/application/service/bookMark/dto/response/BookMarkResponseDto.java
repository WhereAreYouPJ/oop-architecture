package way.application.service.bookMark.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class BookMarkResponseDto {

	public record GetBookMarkResponseDto(
		Long memberSeq,
		String profileImageURL,
		LocalDateTime startTime,
		String location,
		String title,
		List<BookMarkImageInfo> bookMarkImageInfos,
		List<BookMarkFriendInfo> bookMarkFriendInfos,
		String content,
		Boolean bookMark
	) {

	}

	public record BookMarkImageInfo(
		Long feedImageSeq,
		String feedImageURL,
		Integer feedImageOrder
	) {

	}

	public record BookMarkFriendInfo(
			Long memberSeq,
			String userName,
			String profileImageURL
	) {

	}
}

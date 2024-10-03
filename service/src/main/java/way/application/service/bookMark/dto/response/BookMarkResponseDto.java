package way.application.service.bookMark.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class BookMarkResponseDto {
	public record AddBookMarkResponseDto(
		Long bookMarkSeq
	) {

	}

	public record GetBookMarkResponseDto(
		Long memberSeq,
		String profileImage,
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
		Long feedImageOrder
	) {

	}

	public record BookMarkFriendInfo(
			Long memberSeq,
			String profileImageURL
	) {

	}
}

package way.application.service.bookMark.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class BookMarkResponseDto {
	public record AddBookMarkResponseDto(
		Long bookMarkSeq
	) {

	}

	public record GetBookMarkResponseDto(
		String profileImage,
		LocalDateTime startTime,
		String location,
		String title,
		List<String> feedImageUrl,
		String content,
		Boolean bookMark
	) {

	}
}

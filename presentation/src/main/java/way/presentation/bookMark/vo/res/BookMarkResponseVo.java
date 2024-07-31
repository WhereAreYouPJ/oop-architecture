package way.presentation.bookMark.vo.res;

import java.time.LocalDateTime;
import java.util.List;

public class BookMarkResponseVo {
	public record AddBookMarkResponse(
		Long bookMarkSeq
	) {

	}

	public record GetBookMarkResponse(
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

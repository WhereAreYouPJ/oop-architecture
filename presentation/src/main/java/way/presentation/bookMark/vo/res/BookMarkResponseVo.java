package way.presentation.bookMark.vo.res;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class BookMarkResponseVo {
	public record AddBookMarkResponse(
		@Schema(description = "Book Mark DB에 저장한 이후 반환되는 Seq 값")
		Long bookMarkSeq
	) {

	}

	public record GetBookMarkResponse(
		@Schema(description = "피드 작성자 SEQ")
		Long memberSeq,

		@Schema(description = "회원 PROFILE 이미지 URL")
		String profileImage,

		@Schema(description = "Schedule 시작시간")
		LocalDateTime startTime,

		@Schema(description = "Schedule 장소")
		String location,

		@Schema(description = "Schedule 제목")
		String title,

		@Schema(description = "Feed 이미지 URL")
		List<String> feedImageUrl,

		@Schema(description = "Feed 본문")
		String content,

		@Schema(description = "Book Mark 여부")
		Boolean bookMark
	) {

	}
}

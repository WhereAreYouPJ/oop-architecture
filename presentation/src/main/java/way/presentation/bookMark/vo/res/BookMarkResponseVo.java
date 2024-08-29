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
}

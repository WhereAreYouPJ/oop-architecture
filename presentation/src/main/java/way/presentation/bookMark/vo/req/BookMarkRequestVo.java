package way.presentation.bookMark.vo.req;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;

public class BookMarkRequestVo {
	public record AddBookMarRequest(
		@Schema(description = "Book Mark 하고자 하는 Feed Seq")
		Long feedSeq,

		@Schema(description = "저장하는 Member Seq")
		Long memberSeq
	) {
		public AddBookMarkRequestDto toAddBookMarkRequestDto() {
			return new AddBookMarkRequestDto(
				this.feedSeq,
				this.memberSeq
			);
		}
	}

	public record DeleteBookMarkRequest(
		@Schema(description = "Book Mark PK Seq 값")
		Long bookMarkFeedSeq,

		@Schema(description = "삭제하는 Member Seq")
		Long memberSeq
	) {
		public DeleteBookMarkRequestDto toDeleteBookMarkRequestDto() {
			return new DeleteBookMarkRequestDto(
				this.bookMarkFeedSeq,
				this.memberSeq
			);
		}
	}
}

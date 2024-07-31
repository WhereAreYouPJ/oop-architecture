package way.presentation.bookMark.vo.req;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;

public class BookMarkRequestVo {
	public record AddBookMarRequest(
		Long bookMarkFeedSeq,
		Long memberSeq
	) {
		public AddBookMarkRequestDto toAddBookMarkRequestDto() {
			return new AddBookMarkRequestDto(
				this.bookMarkFeedSeq,
				this.memberSeq
			);
		}
	}

	public record DeleteBookMarkRequest(
		Long bookMarkFeedSeq,
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

package way.presentation.bookMark.vo.req;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;

import way.application.service.bookMark.dto.request.BookMarkRequestDto;

public class BookMarkRequestVo {
	public record AddBookMarkRequest(
		Long scheduleSeq,
		Long memberSeq
	) {
		public AddBookMarkRequestDto toAddBookMarkRequestDto() {
			return new AddBookMarkRequestDto(
				this.scheduleSeq,
				this.memberSeq
			);
		}
	}

	public record DeleteBookMarkRequest(
		Long bookMarkScheduleSeq,
		Long memberSeq
	) {
		public DeleteBookMarkRequestDto toDeleteBookMarkRequestDto() {
			return new DeleteBookMarkRequestDto(
				this.bookMarkScheduleSeq,
				this.memberSeq
			);
		}
	}
}

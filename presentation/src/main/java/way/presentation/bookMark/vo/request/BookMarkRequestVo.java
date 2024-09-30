package way.presentation.bookMark.vo.request;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class BookMarkRequestVo {
	public record AddBookMarkRequest(
		Long feedSeq,
		Long memberSeq
	) {
		public AddBookMarkRequestDto toAddBookMarkRequestDto() {
			return new AddBookMarkRequestDto(
				this.feedSeq,
				this.memberSeq
			);
		}

		public void addBookMarkRequestValidate() {
			if (feedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
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

		public void deleteBookMarkRequestValidate() {
			if (bookMarkFeedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}
}
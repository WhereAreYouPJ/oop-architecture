package way.presentation.bookMark.validates;

import static way.presentation.bookMark.vo.req.BookMarkRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class AddBookMarkValidator {
	public void validate(AddBookMarRequest request) {
		validateBookMarkFeedSeq(request.bookMarkFeedSeq());
		validateMemberSeq(request.memberSeq());
	}

	private void validateBookMarkFeedSeq(Long bookMarkFeedSeq) {
		if (bookMarkFeedSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

package way.presentation.feed.validates;

import static way.application.service.feed.dto.request.FeedRequestDto.*;

import org.springframework.stereotype.Component;

import way.application.service.feed.dto.request.FeedRequestDto;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class ModifyFeedValidator {
	public void validate(ModifyFeedRequestDto request) {
		validateFeedSeq(request.feedSeq());
		validateMemberSeq(request.memberSeq());
		validateTitle(request.title());
	}

	private void validateFeedSeq(Long feedSeq) {
		if (feedSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

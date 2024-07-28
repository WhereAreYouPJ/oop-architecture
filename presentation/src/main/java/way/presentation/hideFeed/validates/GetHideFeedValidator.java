package way.presentation.hideFeed.validates;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class GetHideFeedValidator {
	public void validate(Long memberSeq) {
		validateHideFeedSeq(memberSeq);
	}

	private void validateHideFeedSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

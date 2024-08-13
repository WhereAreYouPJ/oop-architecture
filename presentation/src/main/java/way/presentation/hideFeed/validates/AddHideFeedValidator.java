package way.presentation.hideFeed.validates;

import static way.presentation.hideFeed.vo.req.HideFeedRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class AddHideFeedValidator {

	public void validate(HideFeedRequest request) {
		validateHideFeedSeq(request.feedSeq());
		validateMemberSeq(request.memberSeq());
	}

	private void validateHideFeedSeq(Long feedSeq) {
		if (feedSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

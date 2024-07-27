package way.presentation.hideFeed.validates;

import static way.presentation.hideFeed.vo.req.HideFeedRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class DeleteHideFeedValidator {
	public void validate(DeleteHideFeedRequest request) {
		validateHideFeedSeq(request.hideFeedSeq());
		validateMemberSeq(request.memberSeq());
	}

	private void validateHideFeedSeq(Long hideFeedSeq) {
		if (hideFeedSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

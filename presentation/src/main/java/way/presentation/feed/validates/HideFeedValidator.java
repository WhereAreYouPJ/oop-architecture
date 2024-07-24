package way.presentation.feed.validates;

import static way.presentation.feed.vo.req.FeedRequestVo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class HideFeedValidator {
	private static final Logger logger = LoggerFactory.getLogger(HideFeedValidator.class);

	public void validate(HideFeedRequest request) {
		validateFeedSeq(request.feedSeq());
		validateMemberSeq(request.creatorSeq());
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
}

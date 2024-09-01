package way.presentation.feed.validates;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class GetFeedValidator {
	public void validate(Long memberSeq, Long scheduleSeq) {
		validateMemberSeq(memberSeq);
		validateScheduleSeq(scheduleSeq);
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateScheduleSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

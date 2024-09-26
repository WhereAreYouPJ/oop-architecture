package way.presentation.schedule.validates;

import static way.presentation.schedule.vo.request.ScheduleRequestVo.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class SaveScheduleValidator {
	public void validate(SaveScheduleRequest request) {
		validateTitle(request.title());
		validateStartTime(request.startTime());
		validateEndTime(request.endTime());
		validateCreateMemberSeq(request.createMemberSeq());
	}

	private void validateTitle(String title) {
		if (title == null || title.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateStartTime(LocalDateTime startTime) {
		if (startTime == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateEndTime(LocalDateTime endTime) {
		if (endTime == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateCreateMemberSeq(Long createMemberSeq) {
		if (createMemberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

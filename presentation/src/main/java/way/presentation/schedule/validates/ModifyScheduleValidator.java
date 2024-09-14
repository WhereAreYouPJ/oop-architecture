package way.presentation.schedule.validates;

import static way.presentation.schedule.vo.req.ScheduleRequestVo.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.schedule.vo.req.ScheduleRequestVo;

@Component
public class ModifyScheduleValidator {
	public void validate(ModifyScheduleRequest request) {
		validateScheduleSeq(request.scheduleSeq());
		validateTitle(request.title());
		validateStartTime(request.startTime());
		validateEndTime(request.endTime());
		validateCreateMemberSeq(request.createMemberSeq());
	}

	private void validateScheduleSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
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

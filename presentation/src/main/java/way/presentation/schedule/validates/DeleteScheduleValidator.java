package way.presentation.schedule.validates;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.schedule.vo.req.ScheduleRequestVo;

@Component
public class DeleteScheduleValidator {
	public void validate(ScheduleRequestVo.DeleteScheduleRequest request) {
		validateScheduleSeq(request.scheduleSeq());
		validateMemberSeq(request.creatorSeq());
	}

	private void validateScheduleSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

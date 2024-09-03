package way.presentation.chat.validates;

import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class CreateChatRoomValidator {
	public void validate(CreateChatRoomRequest request) {
		validateScheduleSeq(request.scheduleSeq());
	}

	private void validateScheduleSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

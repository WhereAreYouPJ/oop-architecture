package way.presentation.chat.validates;

import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class EnterChatRoomValidator {
	public void validate(EnterChatRoomRequest request) {
		validateMemberSeq(request.memberSeq());
		validateScheduleSeq(request.scheduleSeq());
		validateChatRoomSeq(request.chatRoomSeq());
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

	private void validateChatRoomSeq(String chatRoomSeq) {
		if (chatRoomSeq == null || chatRoomSeq.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

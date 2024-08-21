package way.presentation.chat.validates;

import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class CreateChatRoomValidator {
	public void validate(CreateChatRoomRequest request) {
		validateRoomName(request.roomName());
	}

	private void validateRoomName(String roomName) {
		if (roomName == null || roomName.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

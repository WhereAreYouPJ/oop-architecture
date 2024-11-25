package way.application.service.chat.dto.response;

import java.util.List;

public class ChatResponseDto {
	public record GetChatResponseDto(
		OwnerDto ownerInfo,
		List<MessageDto> messageInfos
	) {

	}

	public record OwnerDto(
		Long memberSeq,
		String userName
	) {

	}

	public record MessageDto(
		Long memberSeq,
		String userName,
		String message
	) {

	}
}

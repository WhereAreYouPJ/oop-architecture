package way.application.service.chat.dto.request;

public class ChatRoomRequestDto {
	public record CreateChatRoomRequestDto(
		Long scheduleSeq
	) {

	}

	public record EnterChatRoomRequestDto(
		Long memberSeq,
		Long scheduleSeq,
		String chatRoomSeq
	) {

	}

	public record SendChatRequestDto(
		String chatRoomSeq,
		Long senderMemberSeq,
		String message
	) {

	}

	public record ExitChatRoomRequestDto(
		Long memberSeq,
		String chatRoomSeq
	) {

	}
}

package way.application.service.chat.dto.request;

public class ChatRoomRequestDto {
	public record CreateChatRoomRequestDto(
		String roomName
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
}

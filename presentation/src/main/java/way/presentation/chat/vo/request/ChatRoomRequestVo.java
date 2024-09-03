package way.presentation.chat.vo.request;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChatRoomRequestVo {
	public record CreateChatRoomRequest(
		Long scheduleSeq
	) {
		public CreateChatRoomRequestDto toCreateChatRoomRequestDto() {
			return new CreateChatRoomRequestDto(
				this.scheduleSeq
			);
		}
	}

	public record EnterChatRoomRequest(
		@Schema(description = "채팅방 입장하려는 Member Seq (Schedule accept = true만 가능)")
		Long memberSeq,

		@Schema(description = "입장하려는 채팅방의 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "입장하려는 채팅방 Seq")
		String chatRoomSeq
	) {
		public EnterChatRoomRequestDto toEnterChatRoomRequestDto() {
			return new EnterChatRoomRequestDto(
				this.memberSeq,
				this.scheduleSeq,
				this.chatRoomSeq
			);
		}
	}

	public record SendChatRequest(
		@Schema(description = "Chat Room Seq")
		String chatRoomSeq,

		@Schema(description = "채팅 전송 Member Seq")
		Long senderMemberSeq,

		@Schema(description = "전송 메세지")
		String message
	) {
		public SendChatRequestDto toSendChatRequestDto() {
			return new SendChatRequestDto(
				this.chatRoomSeq,
				this.senderMemberSeq,
				this.message
			);
		}
	}

	public record ExitChatRoomRequest(
		@Schema(description = "채팅방 나가려는 Member Seq")
		Long memberSeq,

		@Schema(description = "Chat Room Seq")
		String chatRoomSeq
	) {
		public ExitChatRoomRequestDto toExitChatRequestDto() {
			return new ExitChatRoomRequestDto(
				this.memberSeq,
				this.chatRoomSeq
			);
		}
	}
}

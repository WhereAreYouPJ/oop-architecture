package way.presentation.chat.vo.request;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class ChatRoomRequestVo {

	public record EnterChatRoomRequest(
		Long memberSeq,

		Long scheduleSeq,

		String chatRoomSeq
	) {
		public EnterChatRoomRequestDto toEnterChatRoomRequestDto() {
			return new EnterChatRoomRequestDto(
				this.memberSeq,
				this.scheduleSeq,
				this.chatRoomSeq
			);
		}

		public void validateEnterChatRoomRequest() {
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.scheduleSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.chatRoomSeq == null || this.chatRoomSeq.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
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

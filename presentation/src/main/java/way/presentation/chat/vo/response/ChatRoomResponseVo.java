package way.presentation.chat.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChatRoomResponseVo {
	public record CreateChatRoomResponse(
		@Schema(description = "생성된 채팅방 Seq")
		String chatRoomSeq
	) {

	}
}

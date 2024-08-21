package way.application.infrastructure.jpa.chatRoom.repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;

public interface ChatRoomRepository {
	ChatRoomEntity saveChatRoomEntity(ChatRoomEntity chatRoomEntity);

	ChatRoomEntity findByChatRoomSeq(String chatRoomSeq);
}

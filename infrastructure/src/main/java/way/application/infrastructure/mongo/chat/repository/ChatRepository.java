package way.application.infrastructure.mongo.chat.repository;

import java.util.List;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;

public interface ChatRepository {
	ChatEntity saveChatEntity(ChatEntity chatEntity);

	void deleteByChatRoomEntity(ChatRoomEntity chatRoomEntity);

	void deleteByChatRoomEntityAndMemberEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity);

	List<ChatEntity> findAllByChatRoomEntity(ChatRoomEntity chatRoomEntity);
}

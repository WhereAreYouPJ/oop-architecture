package way.application.infrastructure.jpa.chatRoom.repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

public interface ChatRoomMemberRepository {
	ChatRoomMemberEntity saveChatRoomMemberEntity(ChatRoomMemberEntity chatRoomMemberEntity);

	ChatRoomMemberEntity findByMemberEntityAndChatRoomEntity(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity);

	void existsChatRoomMemberEntity(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity);
}

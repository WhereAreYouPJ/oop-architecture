package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.List;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

public interface ChatRoomMemberRepository {
	ChatRoomMemberEntity saveChatRoomMemberEntity(ChatRoomMemberEntity chatRoomMemberEntity);

	ChatRoomMemberEntity findByMemberEntityAndChatRoomEntity(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity);

	void existsChatRoomMemberEntity(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity);

	void deleteAllByChatRoomEntity(ChatRoomEntity chatRoomEntity);

	void deleteByChatRoomEntityAndMemberEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity);

	void deleteAllByMemberSeq(MemberEntity memberEntity);

	void deleteRemainChatRoomMember(ChatRoomEntity chatRoomEntity, List<MemberEntity> memberEntities);
}

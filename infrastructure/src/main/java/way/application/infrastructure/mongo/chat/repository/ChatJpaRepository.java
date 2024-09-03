package way.application.infrastructure.mongo.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;

@Repository
public interface ChatJpaRepository extends JpaRepository<ChatEntity, Long> {
	void deleteAllByChatRoomEntity(ChatRoomEntity chatRoomEntity);

	void deleteByChatRoomEntityAndMemberEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity);
}

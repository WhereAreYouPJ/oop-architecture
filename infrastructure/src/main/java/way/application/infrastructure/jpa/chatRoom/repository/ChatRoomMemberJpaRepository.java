package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Repository
public interface ChatRoomMemberJpaRepository extends JpaRepository<ChatRoomMemberEntity, Long> {
	Optional<ChatRoomMemberEntity> findByMemberEntityAndChatRoomEntity(
		MemberEntity memberEntity,
		ChatRoomEntity chatRoomEntity
	);
}

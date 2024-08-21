package way.application.infrastructure.jpa.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, String> {

}

package way.application.infrastructure.jpa.chatRoom.repository;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

import java.util.List;

public interface ChatRoomRepository {
	ChatRoomEntity saveChatRoomEntity(ChatRoomEntity chatRoomEntity);

	ChatRoomEntity findByChatRoomSeq(String chatRoomSeq);

	void existChatRoomEntityByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteChatRoomEntity(ChatRoomEntity chatRoomEntity);

	ChatRoomEntity findByScheduleEntity(ScheduleEntity scheduleEntity);

    void deleteAllByMemberSeq(List<ScheduleEntity> scheduleEntities);
}

package way.application.service.chat.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.service.chat.mapper.ChatRoomMapper;

@Component
@RequiredArgsConstructor
public class ChatRoomUtils {
	private final ChatRoomRepository chatRoomRepository;

	private final ChatRoomMapper chatRoomMapper;

	public ChatRoomEntity createChatRoomEntity(ScheduleEntity savedScheduleEntity) {
		ChatRoomEntity chatRoomEntity
			= chatRoomMapper.toChatRoomEntity(UUID.randomUUID().toString(), savedScheduleEntity);

		return chatRoomRepository.saveChatRoomEntity(chatRoomEntity);
	}
}

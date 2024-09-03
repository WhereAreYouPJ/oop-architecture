package way.application.infrastructure.mongo.chat.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;

@Component
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
	private final ChatJpaRepository chatMongoRepository;

	@Override
	public ChatEntity saveChatEntity(ChatEntity chatEntity) {
		return chatMongoRepository.save(chatEntity);
	}

	@Override
	public void deleteByChatRoomEntity(ChatRoomEntity chatRoomEntity) {
		chatMongoRepository.deleteAllByChatRoomEntity(chatRoomEntity);
	}

	@Override
	public void deleteByChatRoomEntityAndMemberEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity) {
		chatMongoRepository.deleteByChatRoomEntityAndMemberEntity(chatRoomEntity, memberEntity);
	}
}

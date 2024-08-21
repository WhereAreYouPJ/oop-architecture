package way.application.infrastructure.mongo.chat.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;

@Component
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
	private final ChatJpaRepository chatMongoRepository;

	@Override
	public ChatEntity saveChatEntity(ChatEntity chatEntity) {
		return chatMongoRepository.save(chatEntity);
	}
}

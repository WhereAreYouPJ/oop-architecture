package way.application.infrastructure.mongo.chat.repository;

import way.application.infrastructure.mongo.chat.documents.ChatEntity;

public interface ChatRepository {
	ChatEntity saveChatEntity(ChatEntity chatEntity);
}

package way.application.infrastructure.mongo.chat.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;
import way.application.infrastructure.mongo.chat.documents.QChatEntity;

@Component
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
	private final ChatJpaRepository chatMongoRepository;
	private final JPAQueryFactory queryFactory;

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

	@Override
	public List<ChatEntity> findAllByChatRoomEntity(ChatRoomEntity chatRoomEntity) {
		QChatEntity qChatEntity = QChatEntity.chatEntity;
		QChatRoomMemberEntity qChatRoomMemberEntity = QChatRoomMemberEntity.chatRoomMemberEntity;

		return queryFactory
			.selectFrom(qChatEntity)
			.join(qChatRoomMemberEntity).on(
				qChatRoomMemberEntity.chatRoomEntity.eq(chatRoomEntity),
				qChatRoomMemberEntity.memberEntity.eq(qChatEntity.memberEntity)
			)
			.where(
				qChatEntity.chatRoomEntity.eq(chatRoomEntity)
					.and(qChatEntity.createdAt.after(qChatRoomMemberEntity.createdAt))
			)
			.orderBy(
				qChatEntity.createdAt.desc()
			).fetch();
	}
}

package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public ChatRoomEntity saveChatRoomEntity(ChatRoomEntity chatRoomEntity) {
		return chatRoomJpaRepository.save(chatRoomEntity);
	}

	@Override
	public ChatRoomEntity findByChatRoomSeq(String chatRoomSeq) {
		QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(chatRoomEntity)
				.where(
					chatRoomEntity.chatRoomSeq.eq(chatRoomSeq)
				)
				.fetchOne()
		).orElseThrow(() -> new BadRequestException(ErrorResult.CHAT_ROOM_ID_BAD_REQUEST_EXCEPTION));
	}
}

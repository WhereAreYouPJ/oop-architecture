package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;
import way.application.utils.exception.NotFoundRequestException;

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
		).orElseThrow(() -> new BadRequestException(ErrorResult.CHAT_ROOM_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public void existChatRoomEntityByScheduleEntity(ScheduleEntity scheduleEntity) {
		QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;

		long count = queryFactory
			.selectFrom(chatRoomEntity)
			.where(
				chatRoomEntity.scheduleEntity.eq(scheduleEntity)
			)
			.stream()
			.count();

		if (count > 1) {
			throw new ConflictException(ErrorResult.CHAT_ROOM_DUPLICATION_CONFLICT_EXCEPTION);
		}
	}

	@Override
	public void deleteChatRoomEntity(ChatRoomEntity chatRoomEntity) {
		chatRoomJpaRepository.delete(chatRoomEntity);
	}

	@Override
	public ChatRoomEntity findByScheduleEntity(ScheduleEntity scheduleEntity) {
		QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;

		return Optional.ofNullable(queryFactory
			.selectFrom(chatRoomEntity)
			.where(
				chatRoomEntity.scheduleEntity.eq(scheduleEntity)
			)
			.fetchOne()
		).orElseThrow(() -> new NotFoundRequestException(ErrorResult.UNKNOWN_EXCEPTION));
	}
}

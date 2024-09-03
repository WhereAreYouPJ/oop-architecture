package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberRepositoryImpl implements ChatRoomMemberRepository {
	private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public ChatRoomMemberEntity saveChatRoomMemberEntity(ChatRoomMemberEntity chatRoomMemberEntity) {
		return chatRoomMemberJpaRepository.save(chatRoomMemberEntity);
	}

	@Override
	public void existsChatRoomMemberEntity(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity) {
		chatRoomMemberJpaRepository.findByMemberEntityAndChatRoomEntity(memberEntity, chatRoomEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.CHAT_ROOM_MEMBER_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public void deleteAllByChatRoomEntity(ChatRoomEntity chatRoomEntity) {
		chatRoomMemberJpaRepository.deleteAllByChatRoomEntity(chatRoomEntity);
	}

	@Override
	public void deleteByChatRoomEntityAndMemberEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity) {
		chatRoomMemberJpaRepository.deleteByChatRoomEntityAndMemberEntity(chatRoomEntity, memberEntity);
	}

	@Override
	public ChatRoomMemberEntity findByMemberEntityAndChatRoomEntity(
		MemberEntity memberEntity,
		ChatRoomEntity chatRoomEntity
	) {
		QChatRoomMemberEntity chatRoomMemberEntity = QChatRoomMemberEntity.chatRoomMemberEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(chatRoomMemberEntity)
				.where(
					chatRoomMemberEntity.chatRoomEntity.eq(chatRoomEntity)
						.and(chatRoomMemberEntity.memberEntity.eq(memberEntity))
				)
				.fetchOne()
		).orElseThrow(() -> new BadRequestException(ErrorResult.CHAT_ROOM_DONT_HAVE_MEMBER_NOT_FOUND_EXCEPTION));
	}
}

package way.application.infrastructure.jpa.chatRoom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.QChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.QScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
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
		QChatRoomMemberEntity qChatRoomMemberEntity = QChatRoomMemberEntity.chatRoomMemberEntity;

		Optional.ofNullable(queryFactory
			.selectFrom(qChatRoomMemberEntity)
			.where(
				qChatRoomMemberEntity.chatRoomEntity.eq(chatRoomEntity)
					.and(qChatRoomMemberEntity.memberEntity.eq(memberEntity))
			).fetchOne()
		).orElseThrow(() -> new BadRequestException(ErrorResult.CHAT_ROOM_DONT_HAVE_MEMBER_BAD_REQUEST_EXCEPTION));
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
	public void deleteAllByMemberSeq(MemberEntity memberEntity) {
		QChatRoomMemberEntity chatRoomMemberEntity = QChatRoomMemberEntity.chatRoomMemberEntity;
		QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;
		QScheduleMemberEntity scheduleMemberEntity = QScheduleMemberEntity.scheduleMemberEntity;

		List<ChatRoomMemberEntity> chatRoomMemberEntities = queryFactory.select(chatRoomMemberEntity)
			.from(chatRoomMemberEntity)
			.join(chatRoomEntity).on(chatRoomEntity.chatRoomSeq.eq(chatRoomMemberEntity.chatRoomEntity.chatRoomSeq))
			.join(scheduleMemberEntity).on(chatRoomEntity.scheduleEntity.eq(scheduleMemberEntity.schedule))
			.where(scheduleMemberEntity.isCreator.eq(true)
				.and(scheduleMemberEntity.invitedMember.eq(memberEntity))
			).fetch();

		queryFactory.delete(chatRoomMemberEntity)
			.where(chatRoomMemberEntity.in(chatRoomMemberEntities))
			.execute();

		queryFactory.delete(chatRoomMemberEntity)
			.where(chatRoomMemberEntity.memberEntity.eq(memberEntity))
			.execute();
	}

	@Override
	public void deleteRemainChatRoomMember(ChatRoomEntity chatRoomEntity, List<MemberEntity> memberEntities) {
		QChatRoomMemberEntity chatRoomMember = QChatRoomMemberEntity.chatRoomMemberEntity;

		queryFactory
			.delete(chatRoomMember)
			.where(
				chatRoomMember.chatRoomEntity.eq(chatRoomEntity)
					.and(chatRoomMember.memberEntity.notIn(memberEntities))
			).execute();
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

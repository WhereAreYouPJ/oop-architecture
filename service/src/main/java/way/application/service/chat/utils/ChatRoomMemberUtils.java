package way.application.service.chat.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.domain.member.MemberDomain;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.service.chat.mapper.ChatRoomMemberMapper;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberUtils {
	private final ChatRoomMemberRepository chatRoomMemberRepository;

	private final ChatRoomMemberMapper chatRoomMemberMapper;

	private final MemberDomain memberDomain;

	public void enterChatRoom(MemberEntity memberEntity, ChatRoomEntity chatRoomEntity) {
		ChatRoomMemberEntity chatRoomMemberEntity
			= chatRoomMemberMapper.toChatRoomMemberEntity(memberEntity, chatRoomEntity);

		chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
	}

	public void updateChatRoomMember(
		ChatRoomEntity chatRoomEntity,
		MemberEntity creatorMemberEntity,
		List<MemberEntity> invitedMemberEntities
	) {
		// 기존 Chat Room Member 추출
		Set<ChatRoomMemberEntity> existingChatRoomMemberEntities
			= new HashSet<>(chatRoomMemberRepository.findAllByChatRoomEntity(chatRoomEntity));

		// 기존 Chat Room Member 삭제 (새로 초대된 인원이 아닐 시)
		Set<MemberEntity> newMemberEntities = memberDomain.createMemberSet(creatorMemberEntity, invitedMemberEntities);
		chatRoomMemberRepository.deleteRemainChatRoomMember(chatRoomEntity, newMemberEntities.stream().toList());

		// 새로 들어온 Member 추출
		newMemberEntities.removeIf(invitedMember ->
			existingChatRoomMemberEntities.stream()
				.anyMatch(existingMember ->
					existingMember.getMemberEntity().equals(invitedMember)
				)
		);

		for (MemberEntity newMemberEntity : newMemberEntities) {
			ChatRoomMemberEntity chatRoomMemberEntity
				= chatRoomMemberMapper.toChatRoomMemberEntity(newMemberEntity, chatRoomEntity);

			chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
		}
	}
}

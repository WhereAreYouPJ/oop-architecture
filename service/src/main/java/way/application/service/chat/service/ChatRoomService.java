package way.application.service.chat.service;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepository;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
import way.application.service.chat.dto.response.ChatResponseDto.GetChatResponseDto;
import way.application.service.chat.mapper.ChatMapper;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final ChatRepository chatRepository;

	private final ChatMapper chatMapper;

	@Transactional
	public void createChat(SendChatRequestDto requestDto) {
		/*
		 1. Chat Room 유효성 검사
		 2. Member 유효성 검사
		 3. Chat Room 안에 Member 존재 유효성 검사
		*/
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomSeq(requestDto.chatRoomSeq());
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.senderMemberSeq());
		chatRoomMemberRepository.findByMemberEntityAndChatRoomEntity(memberEntity, chatRoomEntity);

		// Chat Entity 생성 및 저장
		ChatEntity chatEntity = chatMapper.toChatEntity(chatRoomEntity, memberEntity, requestDto.message());

		chatRepository.saveChatEntity(chatEntity);
	}

	@Transactional(readOnly = true)
	public GetChatResponseDto getChatMessage(Long memberSeq, String chatRoomSeq) {
		/*
		 1. Member 유효성 검사
		 2. Chat Room 유효성 검사
		 3. Chat Room 에 해당 Member 존재 여부 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomSeq(chatRoomSeq);
		chatRoomMemberRepository.existsChatRoomMemberEntity(memberEntity, chatRoomEntity);

		/*
		 1. 채팅방에 존재하는 채팅 내용 반환
		    - 생성 시간 기준 내림차순 정렬
		*/
		List<ChatEntity> chatEntities = chatRepository.findAllByChatRoomEntity(chatRoomEntity);
		return chatMapper.toGetChatResponseDto(memberEntity, chatEntities);
	}
}

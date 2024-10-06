package way.application.service.chat.service;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;
import static way.application.service.chat.dto.response.ChatRoomResponseDto.*;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepository;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
import way.application.service.chat.mapper.ChatMapper;
import way.application.service.chat.mapper.ChatRoomMemberMapper;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final ChatRepository chatRepository;

	private final ChatRoomMemberMapper chatRoomMemberMapper;
	private final ChatMapper chatMapper;

	@Transactional
	public void enterChatRoom(EnterChatRoomRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Scheulde Member 유효성 검사
		 4. Chat Room 유효성 검사
		 5. 이미 Chat Room Member 에 존재
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(requestDto.scheduleSeq(), requestDto.memberSeq());
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomSeq(requestDto.chatRoomSeq());
		chatRoomMemberRepository.existsChatRoomMemberEntity(memberEntity, chatRoomEntity);

		// Chat Room Member Entity 생성 및 저장
		ChatRoomMemberEntity chatRoomMemberEntity
			= chatRoomMemberMapper.toChatRoomMemberEntity(memberEntity, chatRoomEntity);
		chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
	}

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
}

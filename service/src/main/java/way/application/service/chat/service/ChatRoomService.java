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
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
import way.application.service.chat.mapper.ChatMapper;
import way.application.service.chat.mapper.ChatRoomMapper;
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

	private final ChatRoomMapper chatRoomMapper;
	private final ChatRoomMemberMapper chatRoomMemberMapper;
	private final ChatMapper chatMapper;

	@Transactional
	public CreateChatRoomResponseDto createChatRoom(CreateChatRoomRequestDto createChatRoomRequestDto) {
		/*
		 1. Schedule 유효성 검사
		 2. Chat Room 이미 존재
		*/
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(createChatRoomRequestDto.scheduleSeq());
		chatRoomRepository.existChatRoomEntityByScheduleEntity(scheduleEntity);

		// Chat Room Entity 생성 및 저장
		ChatRoomEntity chatRoomEntity = chatRoomMapper.toChatRoomEntity(
			UUID.randomUUID().toString(),
			scheduleEntity
		);

		ChatRoomEntity savedChatRoomEntity = chatRoomRepository.saveChatRoomEntity(chatRoomEntity);

		return new CreateChatRoomResponseDto(savedChatRoomEntity.getChatRoomSeq());
	}

	@Transactional
	public void enterChatRoom(EnterChatRoomRequestDto enterChatRoomRequestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Scheulde Member 유효성 검사
		 4. Chat Room 유효성 검사
		 5. 이미 Chat Room Member 에 존재
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(enterChatRoomRequestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(enterChatRoomRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(
			enterChatRoomRequestDto.scheduleSeq(),
			enterChatRoomRequestDto.memberSeq()
		).getSchedule();
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomSeq(enterChatRoomRequestDto.chatRoomSeq());
		chatRoomMemberRepository.existsChatRoomMemberEntity(memberEntity, chatRoomEntity);

		// Chat Room Member Entity 생성 및 저장
		ChatRoomMemberEntity chatRoomMemberEntity
			= chatRoomMemberMapper.toChatRoomMemberEntity(memberEntity, chatRoomEntity, scheduleEntity);
		chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
	}

	@Transactional
	public void createChat(SendChatRequestDto sendChatRequestDto) {
		/*
		 1. Chat Room 유효성 검사
		 2. Member 유효성 검사
		 3. Chat Room 안에 Member 존재 유효성 검사
		*/
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByChatRoomSeq(sendChatRequestDto.chatRoomSeq());
		MemberEntity memberEntity = memberRepository.findByMemberSeq(sendChatRequestDto.senderMemberSeq());
		chatRoomMemberRepository.findByMemberEntityAndChatRoomEntity(memberEntity, chatRoomEntity);

		// Chat Entity 생성 및 저장
		ChatEntity chatEntity = chatMapper.toChatEntity(
			chatRoomEntity,
			memberEntity,
			sendChatRequestDto.message()
		);

		chatRepository.saveChatEntity(chatEntity);
	}
}

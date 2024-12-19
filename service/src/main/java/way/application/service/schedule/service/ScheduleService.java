package way.application.service.schedule.service;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.GetScheduleResponseDto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.friend.FriendDomain;
import way.application.domain.member.MemberDomain;
import way.application.domain.schedule.ScheduleDomain;
import way.application.domain.scheduleMember.ScheduleMemberDomain;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepository;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.friend.respository.FriendRepository;
import way.application.infrastructure.jpa.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberJpaRepository;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
import way.application.service.chat.mapper.ChatRoomMapper;
import way.application.service.chat.mapper.ChatRoomMemberMapper;
import way.application.service.schedule.mapper.ScheduleEntityMapper;
import way.application.service.scheduleMember.mapper.ScheduleMemberMapper;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;
	private final FeedRepository feedRepository;
	private final HideFeedRepository hideFeedRepository;
	private final BookMarkRepository bookMarkRepository;
	private final FeedImageRepository feedImageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final ChatRepository chatRepository;

	private final ScheduleMemberDomain scheduleMemberDomain;
	private final ScheduleDomain scheduleDomain;
	private final FriendDomain friendDomain;
	private final MemberDomain memberDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;

	private final ScheduleEntityMapper scheduleEntityMapper;
	private final ChatRoomMapper chatRoomMapper;
	private final ChatRoomMemberMapper chatRoomMemberMapper;
	private final ScheduleMemberMapper scheduleMemberMapper;

	@Transactional
	public SaveScheduleResponseDto createSchedule(SaveScheduleRequestDto request) {
		/*
		 @Exception

		 1. Member 유효성 검사
		 2. 친구 목록 검사
		*/
		MemberEntity createMemberEntity = memberRepository.findByMemberSeq(request.createMemberSeq());
		List<MemberEntity> invitedMemberEntities = memberRepository.findByMemberSeqs(request.invitedMemberSeqs());
		List<FriendEntity> friendEntities = friendRepository.findByOwner(createMemberEntity);
		friendDomain.checkFriends(invitedMemberEntities, friendEntities);

		/*
		 1. Schedule 저장
		 2. Schedule Member 저장
		 3. Chat Room 생성
		 4. Chat Room Member 생성 (일정 생성자 저장)
		*/
		// Schedule 저장
		ScheduleEntity savedScheduleEntity
			= scheduleRepository.saveSchedule(scheduleEntityMapper.toScheduleEntity(request));

		// Schedule Member 저장
		Set<MemberEntity> invitedMembers = memberDomain.createMemberSet(createMemberEntity, invitedMemberEntities);
		for (MemberEntity invitedMember : invitedMembers) {

			// 일정 생성자 여부 확인 (Domain 처리)
			boolean isCreator = memberDomain.checkIsCreator(invitedMember, createMemberEntity);

			scheduleMemberRepository.saveScheduleMemberEntity(
				scheduleMemberMapper.toScheduleMemberEntity(savedScheduleEntity, invitedMember, isCreator, isCreator)
			);

			if (!isCreator) {
				firebaseNotificationDomain.sendNotification(invitedMember, createMemberEntity);
			}
		}

		// Chat Room 저장
		ChatRoomEntity chatRoomEntity
			= chatRoomMapper.toChatRoomEntity(UUID.randomUUID().toString(), savedScheduleEntity);
		ChatRoomEntity savedChatRoomEntity = chatRoomRepository.saveChatRoomEntity(chatRoomEntity);

		// Chat Room Member 저장
		ChatRoomMemberEntity chatRoomMemberEntity
			= chatRoomMemberMapper.toChatRoomMemberEntity(createMemberEntity, savedChatRoomEntity);

		chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);

		return scheduleEntityMapper.toSaveScheduleResponseDto(
			savedScheduleEntity.getScheduleSeq(), savedChatRoomEntity.getChatRoomSeq()
		);
	}

	@Transactional
	public void modifySchedule(ModifyScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. 초대 Member 유효성 검사
		 3. Schedule 유효성 검사
		 4. Schedule 작성자 확인
		 5. 시작 시간 확인 (전 후 1시간 기준)
		 6. Chat Room 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.createMemberSeq());
		List<MemberEntity> invitedMemberEntities = memberRepository.findByMemberSeqs(requestDto.invitedMemberSeqs());
		scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleIfCreatedByMember(
			requestDto.scheduleSeq(),
			requestDto.createMemberSeq()
		);
		scheduleDomain.validateScheduleStartTime(scheduleEntity.getStartTime());
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByScheduleEntity(scheduleEntity);

		/*
		 1. Schedule Update
		 2. Schedule Member Update
		 3, Chat Room Member Update
		*/

		// Schedule Update
		ScheduleEntity updateScheduleEntity = scheduleEntity.updateScheduleEntity(
			requestDto.title(),
			requestDto.startTime(),
			requestDto.endTime(),
			requestDto.location(),
			requestDto.streetName(),
			requestDto.x(),
			requestDto.y(),
			requestDto.color(),
			requestDto.memo(),
			requestDto.allDay()
		);
		scheduleRepository.saveSchedule(updateScheduleEntity);

		// Schedule Member Udpate
		Set<ScheduleMemberEntity> existingMemberEntities
			= new HashSet<>(scheduleMemberRepository.findAllByScheduleEntity(updateScheduleEntity));

		Set<MemberEntity> newMemberEntities = memberDomain.createMemberSet(memberEntity, invitedMemberEntities);
		scheduleMemberRepository.deleteRemainScheduleEntity(updateScheduleEntity, newMemberEntities.stream().toList());
		chatRoomMemberRepository.deleteRemainChatRoomMember(chatRoomEntity, newMemberEntities.stream().toList());

		newMemberEntities.removeIf(invitedMember ->
			existingMemberEntities.stream()
				.anyMatch(existingMember ->
					existingMember.getInvitedMember().equals(invitedMember)
				)
		);

		for (MemberEntity newMemberEntity : newMemberEntities) {
			firebaseNotificationDomain.sendNotification(newMemberEntity, memberEntity);

			scheduleMemberRepository.saveScheduleMemberEntity(
				scheduleMemberMapper.toScheduleMemberEntity(updateScheduleEntity, newMemberEntity, false, false)
			);
		}

		// Chat Room Member Update
		for (MemberEntity newMemberEntity : newMemberEntities) {
			ChatRoomMemberEntity chatRoomMemberEntity
				= chatRoomMemberMapper.toChatRoomMemberEntity(newMemberEntity, chatRoomEntity);

			chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
		}
	}

	@Transactional
	public void deleteScheduleByCreator(DeleteScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. 생성자 여부 확인
		 4. Chat Room 유효성 검사
		*/
		memberRepository.findByMemberSeq(requestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleIfCreatedByMember(
			requestDto.scheduleSeq(),
			requestDto.memberSeq()
		);
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByScheduleEntity(scheduleEntity);

		/*
		 해당 일정의 모든 Feed 삭제
		 1. Feed Entity
		 2. Hide Feed Entity
		 3. Book Mark Feed Entity
		 4. Feed Image Entity
		 5. Chat Room Member Entity
		 6. Chat Entity
		 7. Chat Room Entity
		*/
		hideFeedRepository.deleteByScheduleEntity(scheduleEntity);
		bookMarkRepository.deleteByScheduleEntity(scheduleEntity);
		feedImageRepository.deleteByScheduleEntity(scheduleEntity);
		feedRepository.deleteByScheduleEntity(scheduleEntity);
		chatRoomMemberRepository.deleteAllByChatRoomEntity(chatRoomEntity);
		chatRepository.deleteByChatRoomEntity(chatRoomEntity);
		chatRoomRepository.deleteChatRoomEntity(chatRoomEntity);

		/*
		 해당 일정의 모든 Schedule 삭제
		 1. Schedule Member
		 2. Schedule
		*/
		scheduleMemberRepository.deleteByScheduleEntity(scheduleEntity);
		scheduleRepository.deleteScheduleEntity(scheduleEntity);
	}

	@Transactional
	public void deleteScheduleMemberByInvitor(DeleteScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schedule 초대자 확인
		 4. Chat Room 유효성 검사
		 TODO 생성자가 아닌 초대자임을 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(
			requestDto.scheduleSeq(),
			requestDto.memberSeq()
		).getSchedule();
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByScheduleEntity(scheduleEntity);

		// 해당 Schedule 의 작성한 Feed 조회
		Optional<FeedEntity> feedEntity
			= feedRepository.findByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);

		/*
		 해당 Member 의 채팅 삭제
		 1. Chat Room Member 삭제
		 2. Chat Room 삭제
		*/
		chatRoomMemberRepository.deleteByChatRoomEntityAndMemberEntity(chatRoomEntity, memberEntity);
		chatRepository.deleteByChatRoomEntityAndMemberEntity(chatRoomEntity, memberEntity);

		if (feedEntity.isEmpty()) {
			scheduleMemberRepository.deleteByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
		} else {
			/*
			 해당 일정의 모든 Feed 삭제
			 1. Feed Entity
			 2. Hide Feed Entity
			 3. Book Mark Feed Entity
			 4. Feed Image Entity
			 5. Chat Room Member Entity
			 6. Chat Entity
			*/
			hideFeedRepository.deleteByFeedEntity(feedEntity.get());
			bookMarkRepository.deleteByFeedEntity(feedEntity.get());
			feedImageRepository.deleteByFeedEntity(feedEntity.get());
			feedRepository.deleteFeedEntity(feedEntity.get());
		}

		/*
		 Feed와 상관 없이
		 1. Schedule Member
		*/
		scheduleMemberRepository.deleteByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
	}

	@Transactional(readOnly = true)
	public GetScheduleResponseDto getSchedule(Long scheduleSeq, Long memberSeq) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schdule Member 존재 유효성 검사
		 4. Start Time 유효성 검사
		*/
		memberRepository.findByMemberSeq(memberSeq);
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(scheduleSeq);
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(scheduleSeq, memberSeq);
		scheduleDomain.validateScheduleStartTime(scheduleEntity.getStartTime());

		/*
		 ScheduleEntity 에서 ScheduleMemberEntity 추출
		 Schedule accept = true 인 경우만
		*/
		List<ScheduleMemberEntity> scheduleEntities
			= scheduleMemberRepository.findAllAcceptedScheduleMembersInSchedule(scheduleEntity);

		// Get Schedule Member Info 추출
		List<GetScheduleMemberInfoDto> memberInfos = scheduleEntityMapper.mapToGetScheduleMemberInfo(scheduleEntities);

		return scheduleEntityMapper.toGetScheduleResponseDto(scheduleEntity, memberInfos);
	}

	@Transactional(readOnly = true)
	public List<GetScheduleByDateResponseDto> getScheduleByDate(Long memberSeq, LocalDate date) {
		/*
		 1. Member 유효성 검사
		*/
		memberRepository.findByMemberSeq(memberSeq);

		/*
		 ScheduleEntity 추출
		 Schedule accept = true 인 경우만
		*/
		List<ScheduleEntity> scheduleEntities
			= scheduleRepository.findAcceptedSchedulesByMemberAndDate(memberSeq, date);

		return scheduleEntities.stream()
			.map(scheduleEntity -> scheduleEntityMapper.toGetScheduleByDateResponseDto(
				scheduleEntity,
				scheduleMemberRepository.findAllAcceptedScheduleMembersInSchedule(scheduleEntity).size() > 1,
				scheduleMemberRepository.findCreatorBySchedule(scheduleEntity)
			)).collect(Collectors.toList());
	}

	@Transactional
	public void acceptSchedule(AcceptScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Chat Room 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByScheduleEntity(scheduleEntity);

		/*
		 1. Schedule Accept True 로 수정
		 2. Chat Room Member 생성
		*/
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findScheduleMemberInSchedule(requestDto.memberSeq(), requestDto.scheduleSeq());
		scheduleMemberEntity.updateAcceptSchedule();
		scheduleMemberRepository.saveScheduleMemberEntity(scheduleMemberEntity);

		ChatRoomMemberEntity chatRoomMemberEntity
			= chatRoomMemberMapper.toChatRoomMemberEntity(memberEntity, chatRoomEntity);
		chatRoomMemberRepository.saveChatRoomMemberEntity(chatRoomMemberEntity);
	}

	@Transactional(readOnly = true)
	public List<GetScheduleByMonthResponseDto> getScheduleByMonth(YearMonth yearMonth, Long memberSeq) {
		/*
		 1. Member 유효성 검사
		*/
		memberRepository.findByMemberSeq(memberSeq);

		// 해당 월 날짜 정보
		LocalDateTime startOfMonth = scheduleDomain.getStartOfMonth(yearMonth);
		LocalDateTime endOfMonth = scheduleDomain.getEndOfMonth(yearMonth);

		List<ScheduleEntity> scheduleEntities
			= scheduleRepository.findSchedulesByYearMonth(startOfMonth, endOfMonth, memberSeq);

		return scheduleEntities.stream()
			.map(scheduleEntityMapper::toGetScheduleByMonthResponseDto)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<GetDdayScheduleResponseDto> getDdaySchedule(Long memberSeq) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		List<ScheduleEntity> scheduleEntities = scheduleRepository.findSchedulesByMember(memberEntity);

		return scheduleEntities.stream()
			.map(scheduleEntity -> scheduleEntityMapper.toGetDdayScheduleResponseDto(
				scheduleEntity,
				scheduleDomain.getDdaySchedule(scheduleEntity.getStartTime())
			)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<GetScheduleListResponseDto> getScheduleList(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		Page<ScheduleEntity> scheduleEntityPage = scheduleRepository.findSchedulesByMemberEntityAndStartTime(
			memberEntity,
			LocalDateTime.now().minusHours(1),
			pageable
		);

		return scheduleEntityPage.map(scheduleEntity -> {
			// Feed Entity 존재 여부 확인
			Boolean feedExists
				= feedRepository.findByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity).isPresent();

			return scheduleEntityMapper.toGetScheduleListResponseDto(scheduleEntity, feedExists);
		});
	}

	@Transactional
	public void refuseSchedule(RefuseScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schedule Member 유효성 검사
		 4. Creator 일 경우 삭제 불가능
		 5. 이미 수락할 경우 삭제 불가능
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		scheduleMemberRepository.findScheduleMemberInSchedule(
			memberEntity.getMemberSeq(),
			scheduleEntity.getScheduleSeq()
		);
		scheduleMemberRepository.validateScheduleMemberIsCreator(memberEntity, scheduleEntity);
		scheduleMemberRepository.validateScheduleMemberAccept(memberEntity, scheduleEntity);

		// ScheduleMember 삭제
		scheduleMemberRepository.deleteByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
	}

	@Transactional(readOnly = true)
	public List<GetInvitedScheduleListResponseDto> getInvitedScheduleList(Long memberSeq) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// 초대 받은 일정 추출
		List<ScheduleMemberEntity> scheduleMemberEntityList
			= scheduleMemberRepository.findInvitedScheduleMemberEntity(memberEntity);
		List<ScheduleEntity> scheduleEntityList
			= scheduleMemberDomain.extractScheduleEntityList(scheduleMemberEntityList);

		return scheduleEntityList.stream()
			.map(scheduleEntity -> {
				Long dDay = scheduleDomain.getDdaySchedule(scheduleEntity.getStartTime());
				return scheduleEntityMapper.toGetInvitedScheduleListResponseDto(scheduleEntity, dDay);
			})
			.collect(Collectors.toList());
	}
}

package way.application.service.schedule.service;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
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
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepository;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.friend.respository.FriendRepository;
import way.application.infrastructure.jpa.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
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

	private final MemberDomain memberDomain;
	private final ScheduleMemberDomain scheduleMemberDomain;
	private final ScheduleDomain scheduleDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;
	private final FriendDomain friendDomain;

	private final ScheduleEntityMapper scheduleEntityMapper;
	private final ScheduleMemberMapper scheduleMemberMapper;

	@Transactional
	public SaveScheduleResponseDto createSchedule(SaveScheduleRequestDto request) {
		/*
		 1. Member 유효성 검사
		 2. 친구 목록 검사
		*/
		MemberEntity createMemberEntity = memberRepository.findByMemberSeq(request.createMemberSeq());
		List<MemberEntity> invitedMemberEntity = memberRepository.findByMemberSeqs(request.invitedMemberSeqs());
		List<FriendEntity> friendEntities = friendRepository.findByOwner(createMemberEntity);
		friendDomain.checkFriends(invitedMemberEntity, friendEntities);

		// Schedule 저장
		ScheduleEntity savedSchedule = scheduleRepository.saveSchedule(scheduleEntityMapper.toScheduleEntity(request));

		// ScheduleMember 저장
		Set<MemberEntity> invitedMembers = memberDomain.createMemberSet(createMemberEntity, invitedMemberEntity);
		for (MemberEntity invitedMember : invitedMembers) {

			// 일정 생성자 여부 확인 (Domain 처리)
			boolean isCreator = memberDomain.checkIsCreator(invitedMember, createMemberEntity);
			scheduleMemberRepository.saveScheduleMemberEntity(
				scheduleMemberMapper.toScheduleMemberEntity(savedSchedule, invitedMember, isCreator, isCreator)
			);

			// 푸시 알림 여부 확인 (Domain 처리)
			if (!isCreator)
				firebaseNotificationDomain.sendNotification(invitedMember, createMemberEntity);
		}

		return new SaveScheduleResponseDto(savedSchedule.getScheduleSeq());
	}

	@Transactional
	public ModifyScheduleResponseDto modifySchedule(ModifyScheduleRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. 초대 Member 유효성 검사
		 3. Schedule 유효성 검사
		 4. Schedule 작성자 확인
		 5. 시작 시간 확인 (전 후 1시간 기준)
		*/
		memberRepository.findByMemberSeq(requestDto.createMemberSeq());
		memberRepository.findByMemberSeqs(requestDto.invitedMemberSeqs());
		scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleIfCreatedByMember(
			requestDto.scheduleSeq(),
			requestDto.createMemberSeq()
		);
		scheduleDomain.validateScheduleStartTime(scheduleEntity.getStartTime());

		// 전체 삭제
		scheduleRepository.deleteById(requestDto.scheduleSeq());
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);

		// 재저장
		SaveScheduleResponseDto saveScheduleResponseDto = createSchedule(requestDto.toSaveScheduleRequestDto());

		return new ModifyScheduleResponseDto(saveScheduleResponseDto.scheduleSeq());
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

	@Cacheable(value = "schedules", key = "#scheduleSeq + '-' + #memberSeq")
	@Transactional(readOnly = true)
	public GetScheduleResponseDto getSchedule(Long scheduleSeq, Long memberSeq) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(memberSeq);
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(scheduleSeq);
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(scheduleSeq, memberSeq);

		// ScheduleEntity 에서 ScheduleMemberEntity 추출
		// Schedule accept = true 인 경우만
		List<ScheduleMemberEntity> scheduleEntities
			= scheduleMemberRepository.findAllAcceptedScheduleMembersInSchedule(scheduleEntity);

		// userName 추출 (Domain Layer)
		List<String> userName = scheduleMemberDomain.extractUserNameFromScheduleMemberEntities(scheduleEntities);

		return new GetScheduleResponseDto(
			scheduleEntity.getTitle(), scheduleEntity.getStartTime(), scheduleEntity.getEndTime(),
			scheduleEntity.getLocation(), scheduleEntity.getStreetName(), scheduleEntity.getX(), scheduleEntity.getY(),
			scheduleEntity.getColor(), scheduleEntity.getMemo(), userName
		);
	}

	@Transactional(readOnly = true)
	public List<GetScheduleByDateResponseDto> getScheduleByDate(
		GetScheduleByDateRequestDto getScheduleByDateRequestDto
	) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(getScheduleByDateRequestDto.memberSeq());

		// ScheduleEntity 추출
		// Schedule accept = true 인 경우만
		List<ScheduleEntity> scheduleEntities = scheduleRepository.findAcceptedSchedulesByMemberAndDate(
			getScheduleByDateRequestDto.memberSeq(), getScheduleByDateRequestDto.date()
		);

		return scheduleEntities.stream()
			.map(scheduleEntity -> new GetScheduleByDateResponseDto(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getTitle(),
				scheduleEntity.getLocation(),
				scheduleEntity.getColor(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getEndTime(),
				scheduleMemberRepository.countBySchedule(scheduleEntity) > 1,
				scheduleEntity.getAllDay())
			).collect(Collectors.toList());
	}

	@Transactional
	public void acceptSchedule(AcceptScheduleRequestDto request) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(request.memberSeq());
		scheduleRepository.findByScheduleSeq(request.scheduleSeq());

		// ScheduleEntity 추출
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findScheduleMemberInSchedule(
			request.memberSeq(),
			request.scheduleSeq()
		);

		scheduleMemberEntity.updateAcceptSchedule();

		scheduleMemberRepository.saveScheduleMemberEntity(scheduleMemberEntity);
	}

	@Transactional(readOnly = true)
	public List<GetScheduleByMonthResponseDto> getScheduleByMonth(
		GetScheduleByMonthRequestDto getScheduleByMonthRequestDto
	) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(getScheduleByMonthRequestDto.memberSeq());

		// Domain 에서 처리
		LocalDateTime startOfMonth = scheduleDomain.getStartOfMonth(getScheduleByMonthRequestDto.yearMonth());
		LocalDateTime endOfMonth = scheduleDomain.getEndOfMonth(getScheduleByMonthRequestDto.yearMonth());

		List<ScheduleEntity> scheduleEntities = scheduleRepository.findSchedulesByYearMonth(
			startOfMonth,
			endOfMonth,
			getScheduleByMonthRequestDto.memberSeq()
		);

		return scheduleEntities.stream()
			.map(scheduleEntity -> new GetScheduleByMonthResponseDto(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getTitle(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getEndTime(),
				scheduleEntity.getLocation(),
				scheduleEntity.getStreetName(),
				scheduleEntity.getX(),
				scheduleEntity.getY(),
				scheduleEntity.getColor(),
				scheduleEntity.getMemo(),
				scheduleEntity.getAllDay()
			))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<GetDdayScheduleResponseDto> getDdaySchedule(GetDdayScheduleDto getDdayScheduleDto) {

		// 유효성 검사 (Repository 에서 처리)
		MemberEntity memberEntity = memberRepository.findByMemberSeq(getDdayScheduleDto.memberSeq());

		List<ScheduleEntity> scheduleEntities = scheduleRepository.findSchedulesByMember(memberEntity);

		return scheduleEntities.stream()
			.map(scheduleEntity -> new GetDdayScheduleResponseDto(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getTitle(),
				scheduleDomain.getDdaySchedule(scheduleEntity.getStartTime())
			))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<GetScheduleListDto> getScheduleList(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		Page<ScheduleEntity> scheduleEntityPage = scheduleRepository.findSchedulesByMemberEntityAndStartTime(
			memberEntity,
			LocalDateTime.now().plusHours(1),
			pageable
		);

		return scheduleEntityPage.map(scheduleEntity -> new GetScheduleListDto(
			scheduleEntity.getScheduleSeq(),
			scheduleEntity.getStartTime(),
			scheduleEntity.getTitle()
		));
	}

	@Transactional
	public void refuseSchedule(RefuseScheduleRequestDto refuseScheduleRequestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schedule Member 유효성 검사
		 4. Creator 일 경우 삭제 불가능
		 5. 이미 수락할 경우 삭제 불가능
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(refuseScheduleRequestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(refuseScheduleRequestDto.scheduleSeq());
		scheduleMemberRepository.findScheduleMemberInSchedule(
			memberEntity.getMemberSeq(),
			scheduleEntity.getScheduleSeq()
		);
		scheduleMemberRepository.validateScheduleMemberIsCreator(memberEntity, scheduleEntity);
		scheduleMemberRepository.validateScheduleMemberAccept(memberEntity, scheduleEntity);

		// ScheduleMember 삭제
		scheduleMemberRepository.deleteByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
	}
}

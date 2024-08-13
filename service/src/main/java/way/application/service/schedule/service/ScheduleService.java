package way.application.service.schedule.service;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.friend.FriendDomain;
import way.application.domain.member.MemberDomain;
import way.application.domain.schedule.ScheduleDomain;
import way.application.domain.scheduleMember.ScheduleMemberDomain;
import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.friend.respository.FriendRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.schedule.mapper.ScheduleMapper;
import way.application.service.scheduleMember.mapper.ScheduleMemberMapper;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final MemberRepository memberRepository;
	private final FriendRepository friendRepository;

	private final MemberDomain memberDomain;
	private final ScheduleMemberDomain scheduleMemberDomain;
	private final ScheduleDomain scheduleDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;
	private final FriendDomain friendDomain;

	private final ScheduleMapper scheduleMapper;
	private final ScheduleMemberMapper scheduleMemberMapper;

	/**
	 * 유효성 검사 -> Repository Interface 에서 처리
	 * 비즈니스 로직 -> Domain 단에서 처리
	 * Service 로직 -> Domain 호출 및 저장
	 *
	 * Service Layer -> Repo의 CRUD만 처리
	 */

	@Transactional
	public SaveScheduleResponseDto createSchedule(SaveScheduleRequestDto saveScheduleRequestDto) {
		/*
		 1. Member 유효성 검사 (Repository 에서 처리)
		 2. 친구 목록 검사
		*/
		MemberEntity createMemberEntity = memberRepository.findByMemberSeq(saveScheduleRequestDto.createMemberSeq());
		List<MemberEntity> invitedMemberEntity = memberRepository.findByMemberSeqs(
			saveScheduleRequestDto.invitedMemberSeqs()
		);
		List<FriendEntity> friendEntities = friendRepository.findByOwner(createMemberEntity);
		friendDomain.checkFriends(invitedMemberEntity, friendEntities);

		// Schedule 저장
		ScheduleEntity savedSchedule = scheduleRepository.saveSchedule(
			scheduleMapper.toScheduleEntity(saveScheduleRequestDto)
		);

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
	public ModifyScheduleResponseDto modifySchedule(ModifyScheduleRequestDto modifyScheduleRequestDto) {
		/*
		 1. Member 유효성 검사
		 2. 초대 Member 유효성 검사
		 3. Schedule 유효성 검사
		 4. 시작 시간 확인 (전 후 1시간 기준)
		*/
		memberRepository.findByMemberSeq(modifyScheduleRequestDto.createMemberSeq());
		memberRepository.findByMemberSeqs(modifyScheduleRequestDto.invitedMemberSeqs());
		scheduleRepository.findByScheduleSeq(modifyScheduleRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleIfCreatedByMember(
			modifyScheduleRequestDto.scheduleSeq(),
			modifyScheduleRequestDto.createMemberSeq()
		);
		scheduleDomain.validateScheduleStartTime(scheduleEntity.getStartTime());

		// 삭제 (Repository 에서 처리)
		scheduleRepository.deleteById(modifyScheduleRequestDto.scheduleSeq());
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);

		// 재저장
		SaveScheduleResponseDto saveScheduleResponseDto = createSchedule(
			modifyScheduleRequestDto.toSaveScheduleRequestDto()
		);

		return new ModifyScheduleResponseDto(saveScheduleResponseDto.scheduleSeq());
	}

	@Transactional
	public void deleteSchedule(DeleteScheduleRequestDto deleteScheduleRequestDto) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(deleteScheduleRequestDto.creatorSeq());
		scheduleRepository.findByScheduleSeq(deleteScheduleRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleIfCreatedByMember(
			deleteScheduleRequestDto.scheduleSeq(),
			deleteScheduleRequestDto.creatorSeq()
		);

		// 전체 데이터 삭제: 연관관계 매핑으로 인해 ScheduleMember -> Schedule 삭제
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);
		scheduleRepository.deleteById(scheduleEntity.getScheduleSeq());
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
				scheduleEntity.getEndTime()))
			.collect(Collectors.toList());
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
				scheduleEntity.getMemo()
			))
			.collect(Collectors.toList());
	}
}

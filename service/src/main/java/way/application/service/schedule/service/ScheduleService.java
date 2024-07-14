package way.application.service.schedule.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.member.MemberDomain;
import way.application.domain.schedule.ScheduleDomain;
import way.application.domain.scheduleMember.ScheduleMemberDomain;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.schedule.dto.request.ScheduleRequestDto;
import way.application.service.schedule.dto.response.ScheduleResponseDto;
import way.application.service.schedule.mapper.ScheduleMapper;
import way.application.service.scheduleMember.mapper.ScheduleMemberMapper;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final MemberRepository memberRepository;

	private final MemberDomain memberDomain;
	private final ScheduleMemberDomain scheduleMemberDomain;
	private final ScheduleDomain scheduleDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;

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
	public ScheduleResponseDto.SaveScheduleResponseDto createSchedule(
		ScheduleRequestDto.SaveScheduleRequestDto saveScheduleRequestDto
	) {
		// Member 유효성 검사 (Repository 에서 처리)
		MemberEntity createMemberEntity = memberRepository.findByMemberSeq(saveScheduleRequestDto.createMemberSeq());
		List<MemberEntity> invitedMemberEntity = memberRepository.findByMemberSeqs(
			saveScheduleRequestDto.invitedMemberSeqs()
		);

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

		return new ScheduleResponseDto.SaveScheduleResponseDto(savedSchedule.getScheduleSeq());
	}

	@Transactional
	public ScheduleResponseDto.ModifyScheduleResponseDto modifySchedule(
		ScheduleRequestDto.ModifyScheduleRequestDto modifyScheduleRequestDto
	) {
		// 유효성 검사 (Repository 에서 처리)
		MemberEntity createMemberEntity = memberRepository.findByMemberSeq(
			modifyScheduleRequestDto.createMemberSeq()
		);
		List<MemberEntity> invitedMemberEntity = memberRepository.findByMemberSeqs(
			modifyScheduleRequestDto.invitedMemberSeqs()
		);
		ScheduleEntity savedSchedule = scheduleRepository.findByScheduleSeq(modifyScheduleRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleByCreator(
			modifyScheduleRequestDto.scheduleSeq(),
			modifyScheduleRequestDto.createMemberSeq()
		);

		// 삭제 (Repository 에서 처리)
		scheduleRepository.deleteById(modifyScheduleRequestDto.scheduleSeq());
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);

		// 재저장
		ScheduleResponseDto.SaveScheduleResponseDto saveScheduleResponseDto = createSchedule(
			modifyScheduleRequestDto.toSaveScheduleRequestDto()
		);

		return new ScheduleResponseDto.ModifyScheduleResponseDto(saveScheduleResponseDto.scheduleSeq());
	}

	@Transactional
	public void deleteSchedule(ScheduleRequestDto.DeleteScheduleRequestDto deleteScheduleRequestDto) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(deleteScheduleRequestDto.creatorSeq());
		scheduleRepository.findByScheduleSeq(deleteScheduleRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.findScheduleByCreator(
			deleteScheduleRequestDto.scheduleSeq(),
			deleteScheduleRequestDto.creatorSeq()
		);

		// 전체 데이터 삭제: 연관관계 매핑으로 인해 ScheduleMember -> Schedule 삭제
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);
		scheduleRepository.deleteById(scheduleEntity.getScheduleSeq());
	}

	@Cacheable(value = "schedules", key = "#scheduleSeq + '-' + #memberSeq")
	@Transactional(readOnly = true)
	public ScheduleResponseDto.GetScheduleResponseDto getSchedule(Long scheduleSeq, Long memberSeq) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(memberSeq);
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(scheduleSeq);
		scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(scheduleSeq, memberSeq);

		// ScheduleEntity 에서 ScheduleMemberEntity 추출
		// Schedule accept = true 인 경우만
		List<ScheduleMemberEntity> scheduleEntities
			= scheduleMemberRepository.findAcceptedScheduleMemberByScheduleEntity(scheduleEntity);

		// userName 추출 (Domain Layer)
		List<String> userName = scheduleMemberDomain.extractUserNameFromScheduleMemberEntities(scheduleEntities);

		return new ScheduleResponseDto.GetScheduleResponseDto(
			scheduleEntity.getTitle(), scheduleEntity.getStartTime(), scheduleEntity.getEndTime(),
			scheduleEntity.getLocation(), scheduleEntity.getStreetName(), scheduleEntity.getX(), scheduleEntity.getY(),
			scheduleEntity.getColor(), scheduleEntity.getMemo(), userName
		);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponseDto.GetScheduleByDateResponseDto> getScheduleByDate(
		ScheduleRequestDto.GetScheduleByDateRequestDto getScheduleByDateRequestDto
	) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(getScheduleByDateRequestDto.memberSeq());

		// ScheduleEntity 추출
		// Schedule accept = true 인 경우만
		List<ScheduleEntity> scheduleEntities = scheduleRepository.findAcceptedSchedulesByMemberAndDate(
			getScheduleByDateRequestDto.memberSeq(), getScheduleByDateRequestDto.date()
		);

		return scheduleEntities.stream()
			.map(scheduleEntity -> new ScheduleResponseDto.GetScheduleByDateResponseDto(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getTitle(),
				scheduleEntity.getLocation(),
				scheduleEntity.getColor(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getEndTime()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public void acceptSchedule(ScheduleRequestDto.AcceptScheduleRequestDto request) {
		// 유효성 검사 (Repository 에서 처리)
		memberRepository.findByMemberSeq(request.memberSeq());
		scheduleRepository.findByScheduleSeq(request.scheduleSeq());

		// ScheduleEntity 추출
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findScheduleMemberEntityByMemberSeqAndScheduleSeq(
			request.memberSeq(),
			request.scheduleSeq()
		);

		scheduleMemberEntity.updateAcceptSchedule();

		scheduleMemberRepository.saveScheduleMemberEntity(scheduleMemberEntity);
	}

	public List<ScheduleResponseDto.GetScheduleByMonthResponseDto> getScheduleByMonth(
		ScheduleRequestDto.GetScheduleByMonthRequestDto getScheduleByMonthRequestDto
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
			.map(scheduleEntity -> new ScheduleResponseDto.GetScheduleByMonthResponseDto(
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

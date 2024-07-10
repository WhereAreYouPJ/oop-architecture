package way.application.service.schedule.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.member.MemberDomain;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.schedule.dto.request.DeleteScheduleRequestDto;
import way.application.service.schedule.dto.request.ModifyScheduleRequestDto;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;
import way.application.service.schedule.dto.response.ModifyScheduleResponseDto;
import way.application.service.schedule.dto.response.SaveScheduleResponseDto;
import way.application.service.schedule.mapper.ScheduleMapper;
import way.application.service.scheduleMember.mapper.ScheduleMemberMapper;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final MemberRepository memberRepository;

	private final MemberDomain memberDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;

	private final ScheduleMapper scheduleMapper;
	private final ScheduleMemberMapper scheduleMemberMapper;

	/**
	 * @param saveScheduleRequestDto
	 *
	 * 유효성 검사 -> Repository Interface 에서 처리
	 * 비즈니스 로직 -> Domain 단에서 처리
	 * Service 로직 -> Domain 호출 및 저장
	 */
	@Transactional
	public SaveScheduleResponseDto createSchedule(SaveScheduleRequestDto saveScheduleRequestDto) {
		// Member 유효성 검사 (Repository 에서 처리)
		MemberEntity createMemberEntity = memberRepository.validateMemberSeq(saveScheduleRequestDto.createMemberSeq());
		List<MemberEntity> invitedMemberEntity = memberRepository.validateMemberSeqs(
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

		return new SaveScheduleResponseDto(savedSchedule.getScheduleSeq());
	}

	@Transactional
	public ModifyScheduleResponseDto modifySchedule(ModifyScheduleRequestDto modifyScheduleRequestDto) {
		// 유효성 검사 (Repository 에서 처리)
		MemberEntity createMemberEntity = memberRepository.validateMemberSeq(
			modifyScheduleRequestDto.createMemberSeq()
		);
		List<MemberEntity> invitedMemberEntity = memberRepository.validateMemberSeqs(
			modifyScheduleRequestDto.invitedMemberSeqs()
		);
		ScheduleEntity scheduleEntity = scheduleMemberRepository.validateScheduleEntityCreatedByMember(
			modifyScheduleRequestDto.scheduleSeq(),
			modifyScheduleRequestDto.createMemberSeq()
		);

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
		memberRepository.validateMemberSeq(deleteScheduleRequestDto.creatorSeq());
		scheduleRepository.validateScheduleSeq(deleteScheduleRequestDto.scheduleSeq());
		ScheduleEntity scheduleEntity = scheduleMemberRepository.validateScheduleEntityCreatedByMember(
			deleteScheduleRequestDto.scheduleSeq(),
			deleteScheduleRequestDto.creatorSeq()
		);

		// 전체 데이터 삭제: 연관관계 매핑으로 인해 ScheduleMember -> Schedule 삭제
		scheduleMemberRepository.deleteAllBySchedule(scheduleEntity);
		scheduleRepository.deleteById(scheduleEntity.getScheduleSeq());
	}
}

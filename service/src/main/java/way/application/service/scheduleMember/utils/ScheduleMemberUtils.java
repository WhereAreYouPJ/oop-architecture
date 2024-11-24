package way.application.service.scheduleMember.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.member.MemberDomain;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.scheduleMember.mapper.ScheduleMemberMapper;

@Component
@RequiredArgsConstructor
public class ScheduleMemberUtils {
	private final ScheduleMemberRepository scheduleMemberRepository;

	private final MemberDomain memberDomain;
	private final FirebaseNotificationDomain firebaseNotificationDomain;

	private final ScheduleMemberMapper scheduleMemberMapper;

	public void createScheduleMemberEntity(
		MemberEntity createMemberEntity,
		List<MemberEntity> invitedMemberEntity,
		ScheduleEntity savedScheduleEntity
	) {
		Set<MemberEntity> invitedMembers = memberDomain.createMemberSet(createMemberEntity, invitedMemberEntity);
		for (MemberEntity invitedMember : invitedMembers) {

			// 일정 생성자 여부 확인 (Domain 처리)
			boolean isCreator = memberDomain.checkIsCreator(invitedMember, createMemberEntity);
			scheduleMemberRepository.saveScheduleMemberEntity(
				scheduleMemberMapper.toScheduleMemberEntity(savedScheduleEntity, invitedMember, isCreator, isCreator)
			);

			// 푸시 알림 여부 확인 (Domain 처리)
			if (!isCreator)
				firebaseNotificationDomain.sendNotification(invitedMember, createMemberEntity);
		}
	}

	public void updateScheduleMemberEntity(
		MemberEntity createMemberEntity,
		List<MemberEntity> invitedMemberEntity,
		ScheduleEntity savedScheduleEntity
	) {
		Set<ScheduleMemberEntity> existingMemberEntities
			= new HashSet<>(scheduleMemberRepository.findAllByScheduleEntity(savedScheduleEntity));

		// 기존 Schedule Member 삭제 (새로 초대된 인원이 아닐 시)
		Set<MemberEntity> newMemberEntities = memberDomain.createMemberSet(createMemberEntity, invitedMemberEntity);
		scheduleMemberRepository.deleteRemainScheduleEntity(savedScheduleEntity, newMemberEntities.stream().toList());

		// 새로 들어온 Member 추출
		newMemberEntities.removeIf(invitedMember ->
			existingMemberEntities.stream()
				.anyMatch(existingMember ->
					existingMember.getInvitedMember().equals(invitedMember)
				)
		);

		for (MemberEntity newMemberEntity : newMemberEntities) {
			scheduleMemberRepository.saveScheduleMemberEntity(
				scheduleMemberMapper.toScheduleMemberEntity(savedScheduleEntity, newMemberEntity, false, false)
			);
		}

		for (MemberEntity newMemberEntity : newMemberEntities) {
			firebaseNotificationDomain.sendNotification(newMemberEntity, createMemberEntity);
		}
	}
}

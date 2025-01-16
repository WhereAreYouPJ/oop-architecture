package way.application.infrastructure.jpa.scheduleMember.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;

public interface ScheduleMemberRepository {
	ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity);

	ScheduleEntity findScheduleIfCreatedByMember(Long scheduleSeq, Long memberSeq);

	void deleteAllBySchedule(ScheduleEntity scheduleEntity);

	ScheduleMemberEntity findAcceptedScheduleMemberInSchedule(Long scheduleSeq, Long memberSeq);

	List<ScheduleMemberEntity> findAllAcceptedScheduleMembersInSchedule(ScheduleEntity scheduleEntity);

	List<ScheduleMemberEntity> findAllAcceptedScheduleMembersFriendsInSchedule(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity
	);

	ScheduleMemberEntity findScheduleMemberInSchedule(Long memberSeq, Long scheduleSeq);

	List<ScheduleMemberEntity> findByMemberEntity(MemberEntity memberEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);

	void validateScheduleMemberIsCreator(MemberEntity memberEntity, ScheduleEntity scheduleEntity);

	void validateScheduleMemberAccept(MemberEntity memberEntity, ScheduleEntity scheduleEntity);

	List<ScheduleMemberEntity> findInvitedScheduleMemberEntity(MemberEntity memberEntity);

	Long findCreatorBySchedule(ScheduleEntity scheduleEntity);

	List<ScheduleEntity> findSchedulesIfCreatedByMember(MemberEntity memberEntity);

	void deleteAllByMemberSeq(MemberEntity memberEntity, List<ScheduleEntity> scheduleEntities);

	List<ScheduleMemberEntity> findAllByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteRemainScheduleEntity(ScheduleEntity scheduleEntity, List<MemberEntity> memberEntities);
}

package way.application.infrastructure.scheduleMember.repository;

import java.util.List;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

public interface ScheduleMemberRepository {
	// ScheduleMemberEntity 저장
	ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity);

	// ScheduleEntity 가 해당 memberSeq 가 creator 인지 확인
	ScheduleEntity validateScheduleEntityCreatedByMember(Long scheduleSeq, Long memberSeq);

	// ScheduleEntity 전체 삭제
	void deleteAllBySchedule(ScheduleEntity scheduleEntity);

	// Schedule 수락 O, 해당 Schedule에 Member O -> ScheduleMemberEntity 반환
	ScheduleMemberEntity findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(Long scheduleSeq, Long memberSeq);

	// 해당 Schedule 을 수락한 모든 ScheduleEntity 반환
	List<ScheduleMemberEntity> findAcceptedScheduleMemberByScheduleEntity(ScheduleEntity scheduleEntity);
}

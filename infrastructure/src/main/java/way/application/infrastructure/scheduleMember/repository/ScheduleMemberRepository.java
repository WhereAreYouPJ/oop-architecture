package way.application.infrastructure.scheduleMember.repository;

import java.util.List;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

public interface ScheduleMemberRepository {
	ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity);

	ScheduleEntity validateScheduleEntityCreatedByMember(Long scheduleSeq, Long memberSeq);

	void deleteAllBySchedule(ScheduleEntity scheduleEntity);

	ScheduleMemberEntity findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(Long scheduleSeq, Long memberSeq);

	List<ScheduleMemberEntity> findAcceptedScheduleMemberByScheduleEntity(ScheduleEntity scheduleEntity);
}

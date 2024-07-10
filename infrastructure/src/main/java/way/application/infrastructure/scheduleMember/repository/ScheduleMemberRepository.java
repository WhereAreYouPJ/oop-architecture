package way.application.infrastructure.scheduleMember.repository;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

public interface ScheduleMemberRepository {
	ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity);
	ScheduleEntity validateScheduleEntityCreatedByMember(Long scheduleSeq, Long memberSeq);
	void deleteAllBySchedule(ScheduleEntity scheduleEntity);
}

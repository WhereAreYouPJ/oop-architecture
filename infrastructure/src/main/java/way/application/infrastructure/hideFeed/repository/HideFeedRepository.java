package way.application.infrastructure.hideFeed.repository;

import java.util.List;

import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface HideFeedRepository {
	void save(HideFeedEntity hideFeedEntity);
	void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

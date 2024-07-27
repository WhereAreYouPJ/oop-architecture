package way.application.infrastructure.feed.repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);
	FeedEntity findByFeedSeq(Long feedSeq);
	FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq);
	void findByCreatorMemberAndSchedule(MemberEntity creatorMemberEntity, ScheduleEntity scheduleEntity);
	void deleteAllByFeedSeq(Long feedSeq);
}

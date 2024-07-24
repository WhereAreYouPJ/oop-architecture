package way.application.infrastructure.feed.repository;

import java.util.List;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);
	FeedEntity findByFeedSeq(Long feedSeq);
	FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq);
	void deleteAllByFeedSeq(Long feedSeq);
}

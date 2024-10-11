package way.application.infrastructure.jpa.feedImage.repository;

import java.util.List;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface FeedImageRepository {
	FeedImageEntity saveFeedImageEntity(FeedImageEntity feedImageEntity);

	void deleteAllByFeedEntity(FeedEntity feedEntity);

	List<FeedImageEntity> findAllByFeedEntity(FeedEntity feedEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteByFeedEntity(FeedEntity feedEntity);

    void deleteAllByMemberSeq(MemberEntity memberEntity);
}

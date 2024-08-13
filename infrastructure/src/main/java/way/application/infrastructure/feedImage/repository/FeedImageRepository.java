package way.application.infrastructure.feedImage.repository;

import java.util.List;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface FeedImageRepository {
	FeedImageEntity saveFeedImageEntity(FeedImageEntity feedImageEntity);

	void deleteAllByFeedEntity(FeedEntity feedEntity);

	List<FeedImageEntity> findAllByFeedEntity(FeedEntity feedEntity);

	List<String> findFeedImageURLsByFeedEntity(FeedEntity feedEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);
}

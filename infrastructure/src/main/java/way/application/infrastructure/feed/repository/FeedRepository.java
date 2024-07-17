package way.application.infrastructure.feed.repository;

import way.application.infrastructure.feed.entity.FeedEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);
}

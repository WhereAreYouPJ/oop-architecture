package way.application.infrastructure.feedImage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;

@Repository
public interface FeedImageJpaRepository extends JpaRepository<FeedImageEntity, Long> {
	void deleteAllByFeedEntity(FeedEntity feedEntity);
	List<FeedImageEntity> findAllByFeedEntity(FeedEntity feedEntity);
}

package way.application.infrastructure.jpa.feedImage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;

@Repository
public interface FeedImageJpaRepository extends JpaRepository<FeedImageEntity, Long> {
	void deleteAllByFeedEntity(FeedEntity feedEntity);
	List<FeedImageEntity> findAllByFeedEntity(FeedEntity feedEntity);
}

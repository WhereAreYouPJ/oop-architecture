package way.application.infrastructure.feedImage.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;

@Component
@RequiredArgsConstructor
public class FeedImageRepositoryImpl implements FeedImageRepository {
	private final FeedImageJpaRepository feedImageJpaRepository;

	@Override
	public FeedImageEntity saveFeedImageEntity(FeedImageEntity feedImageEntity) {
		return feedImageJpaRepository.save(feedImageEntity);
	}

	@Override
	public void deleteAllByFeedEntity(FeedEntity feedEntity) {
		feedImageJpaRepository.deleteAllByFeedEntity(feedEntity);
	}

	@Override
	public List<FeedImageEntity> findAllByFeedEntity(FeedEntity feedEntity) {
		return feedImageJpaRepository.findAllByFeedEntity(feedEntity);
	}
}

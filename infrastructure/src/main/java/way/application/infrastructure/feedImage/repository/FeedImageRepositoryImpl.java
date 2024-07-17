package way.application.infrastructure.feedImage.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;

@Component
@RequiredArgsConstructor
public class FeedImageRepositoryImpl implements FeedImageRepository {
	private final FeedImageJpaRepository feedImageJpaRepository;

	@Override
	public FeedImageEntity saveFeedImageEntity(FeedImageEntity feedImageEntity) {
		return feedImageJpaRepository.save(feedImageEntity);
	}
}

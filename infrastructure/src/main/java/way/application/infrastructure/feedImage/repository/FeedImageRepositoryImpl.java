package way.application.infrastructure.feedImage.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.feedImage.entity.QFeedImageEntity;

@Component
@RequiredArgsConstructor
public class FeedImageRepositoryImpl implements FeedImageRepository {
	private final FeedImageJpaRepository feedImageJpaRepository;
	private final JPAQueryFactory queryFactory;

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

	@Override
	public List<String> findFeedImageURLsByFeedEntity(FeedEntity feedEntity) {
		QFeedImageEntity feedImageEntity = QFeedImageEntity.feedImageEntity;

		return queryFactory
			.select(feedImageEntity.feedImageURL)
			.from(feedImageEntity)
			.where(feedImageEntity.feedEntity.eq(feedEntity))
			.fetch();
	}
}

package way.application.domain.feedImage;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feedImage.entity.QFeedImageEntity;

@Component
@RequiredArgsConstructor
public class FeedImageDomain {
	private final JPAQueryFactory queryFactory;

	public List<String> findFeedImageURLsByFeedEntity(FeedEntity feedEntity) {
		QFeedImageEntity feedImageEntity = QFeedImageEntity.feedImageEntity;

		return queryFactory
			.select(feedImageEntity.feedImageURL)
			.from(feedImageEntity)
			.where(feedImageEntity.feedEntity.eq(feedEntity))
			.fetch();
	}
}

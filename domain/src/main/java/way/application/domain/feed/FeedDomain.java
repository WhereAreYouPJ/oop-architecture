package way.application.domain.feed;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.entity.QFeedEntity;
import way.application.infrastructure.hideFeed.entity.QHideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Component
@RequiredArgsConstructor
public class FeedDomain {
	private final JPAQueryFactory queryFactory;

	public Page<FeedEntity> findByScheduleExcludingHidden(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity,
		Pageable pageable
	) {
		QFeedEntity feed = QFeedEntity.feedEntity;
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		QueryResults<FeedEntity> results = queryFactory
			.selectFrom(feed)
			.leftJoin(hideFeed)
			.on(feed.eq(hideFeed.feedEntity)
				.and(hideFeed.memberEntity.eq(memberEntity)))
			.where(feed.schedule.eq(scheduleEntity)
				.and(hideFeed.feedEntity.isNull()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		List<FeedEntity> content = results.getResults();
		long total = results.getTotal();

		return new PageImpl<>(content, pageable, total);
	}
}

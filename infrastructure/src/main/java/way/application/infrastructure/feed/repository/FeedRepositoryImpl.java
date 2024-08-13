package way.application.infrastructure.feed.repository;

import java.util.List;
import java.util.Optional;

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
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepository {
	private final FeedJpaRepository feedJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public FeedEntity saveFeedEntity(FeedEntity feedEntity) {
		return feedJpaRepository.save(feedEntity);
	}

	@Override
	public FeedEntity findByFeedSeq(Long feedSeq) {
		return feedJpaRepository.findById(feedSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq) {
		return feedJpaRepository.findFeedEntityByCreatorMemberAndFeedSeq(creatorMemberEntity, feedSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public void findByCreatorMemberAndSchedule(MemberEntity creatorMemberEntity, ScheduleEntity scheduleEntity) {
		feedJpaRepository.findFeedEntityByCreatorMemberAndSchedule(creatorMemberEntity, scheduleEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public void deleteAllByFeedSeq(Long feedSeq) {
		feedJpaRepository.deleteAllByFeedSeq(feedSeq);
	}

	@Override
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

	@Override
	public void deleteFeedEntity(FeedEntity feedEntity) {
		feedJpaRepository.delete(feedEntity);
	}

	@Override
	public void deleteByScheduleEntity(ScheduleEntity scheduleEntity) {
		QFeedEntity feed = QFeedEntity.feedEntity;

		queryFactory
			.delete(feed)
			.where(
				feed.schedule.eq(scheduleEntity)
			)
			.execute();
	}

	@Override
	public Optional<FeedEntity> findByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity,
		MemberEntity memberEntity) {
		QFeedEntity feed = QFeedEntity.feedEntity;

		return Optional.ofNullable(queryFactory
			.select(feed)
			.from(feed)
			.where(
				feed.schedule.eq(scheduleEntity)
					.and(feed.creatorMember.eq(memberEntity))
			)
			.fetchOne()
		);
	}
}

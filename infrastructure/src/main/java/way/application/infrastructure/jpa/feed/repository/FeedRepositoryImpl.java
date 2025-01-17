package way.application.infrastructure.jpa.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.entity.QFeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.QHideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
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
	public FeedEntity findByScheduleExcludingHiddenRand(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity
	) {
		QFeedEntity feed = QFeedEntity.feedEntity;
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		return queryFactory
			.selectFrom(feed)
			.leftJoin(hideFeed)
			.on(feed.eq(hideFeed.feedEntity)
				.and(hideFeed.memberEntity.eq(memberEntity)))
			.where(feed.schedule.eq(scheduleEntity)
				.and(hideFeed.feedEntity.isNull()))
			.orderBy(
				Expressions.booleanTemplate(
					"case when {0} = {1} then 1 else 0 end", feed.creatorMember, memberEntity
				).desc()
			)
			.fetchFirst();
	}

	@Override
	public FeedEntity findFeedEntityExcludingCreatorMember(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity
	) {
		QFeedEntity feed = QFeedEntity.feedEntity;
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		// Member가 작성한 Feed가 아닌 무작위 Feed를 반환
		return queryFactory
			.selectFrom(feed)
			.leftJoin(hideFeed)
			.on(feed.eq(hideFeed.feedEntity)
				.and(hideFeed.memberEntity.eq(memberEntity)))
			.where(
				feed.schedule.eq(scheduleEntity)
					.and(hideFeed.feedEntity.isNull())
					.and(feed.creatorMember.ne(memberEntity))
			) // 작성자 제외 조건
			.fetchFirst();
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
	public Optional<FeedEntity> findByScheduleEntityAndMemberEntity(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity
	) {
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

	@Override
	public List<FeedEntity> findByScheduleEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity) {
		QFeedEntity feed = QFeedEntity.feedEntity;
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		return queryFactory
			.selectFrom(feed)
			.leftJoin(hideFeed)
			.on(feed.eq(hideFeed.feedEntity)
				.and(hideFeed.memberEntity.eq(memberEntity)))
			.where(feed.schedule.eq(scheduleEntity)
				.and(hideFeed.feedEntity.isNull()))
			.orderBy(
				Expressions.booleanTemplate(
					"case when {0} = {1} then 1 else 0 end", feed.creatorMember, memberEntity
				).desc()
			)
			.fetch();
	}

	@Override
	public void deleteAllByMemberSeq(MemberEntity memberEntity) {
		QFeedEntity feed = QFeedEntity.feedEntity;

		queryFactory
			.delete(feed)
			.where(feed.creatorMember.eq(memberEntity))
			.execute();
	}
}

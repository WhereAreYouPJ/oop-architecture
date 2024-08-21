package way.application.infrastructure.jpa.hideFeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.QHideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class HideFeedRepositoryImpl implements HideFeedRepository {
	private final HideFeedJpaRepository hideFeedJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public HideFeedEntity saveHideFeedEntity(HideFeedEntity hideFeedEntity) {
		return hideFeedJpaRepository.save(hideFeedEntity);
	}

	@Override
	public void deleteHideFeedEntity(HideFeedEntity hideFeedEntity) {
		hideFeedJpaRepository.delete(hideFeedEntity);
	}

	@Override
	public void verifyHideFeedNotExists(FeedEntity feedEntity, MemberEntity memberEntity) {
		hideFeedJpaRepository.findHideFeedEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public Page<HideFeedEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	) {
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;

		QueryResults<HideFeedEntity> results = queryFactory
			.selectFrom(hideFeed)
			.join(hideFeed.feedEntity.schedule, schedule)
			.where(hideFeed.memberEntity.eq(memberEntity))
			.orderBy(schedule.startTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	@Override
	public void deleteByScheduleEntity(ScheduleEntity scheduleEntity) {
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		queryFactory
			.delete(hideFeed)
			.where(
				hideFeed.feedEntity.schedule.eq(scheduleEntity)
			)
			.execute();
	}

	@Override
	public void deleteByFeedEntity(FeedEntity feedEntity) {
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		queryFactory
			.delete(hideFeed)
			.where(
				hideFeed.feedEntity.eq(feedEntity)
			)
			.execute();
	}

	@Override
	public HideFeedEntity findByHideFeedSeq(Long hideFeedSeq) {
		return hideFeedJpaRepository.findById(hideFeedSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.HIDE_FEED_BAD_REQUEST_EXCEPTION));
	}
}

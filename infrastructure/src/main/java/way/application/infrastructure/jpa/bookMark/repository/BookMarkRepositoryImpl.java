package way.application.infrastructure.jpa.bookMark.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.bookMark.entity.QBookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.entity.QFeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.QHideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository {
	private final BookMarkJpaRepository bookMarkJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public BookMarkEntity saveBookMarkEntity(BookMarkEntity bookMarkEntity) {
		return bookMarkJpaRepository.save(bookMarkEntity);
	}

	@Override
	public void deleteBookMarkEntity(BookMarkEntity bookMarkEntity) {
		bookMarkJpaRepository.delete(bookMarkEntity);
	}

	@Override
	public void checkBookMarkFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		bookMarkJpaRepository.findBookMarkEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.BOOK_MARK_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		return bookMarkJpaRepository.existsByFeedEntityAndMemberEntity(feedEntity, memberEntity);
	}

	@Override
	public Page<BookMarkEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;
		QFeedEntity feed = QFeedEntity.feedEntity;
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;
		QHideFeedEntity hideFeed = QHideFeedEntity.hideFeedEntity;

		QueryResults<BookMarkEntity> results = queryFactory
			.selectFrom(bookMark)
			.join(bookMark.feedEntity, feed)
			.join(feed.schedule, schedule)
			.leftJoin(hideFeed).on(
				hideFeed.feedEntity.eq(feed)
					.and(hideFeed.memberEntity.eq(memberEntity))
			)
			.where(
				bookMark.memberEntity.eq(memberEntity),
				hideFeed.isNull() // hide-feed가 없는 경우만 포함
			)
			.orderBy(schedule.startTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	@Override
	public void deleteByScheduleEntity(ScheduleEntity scheduleEntity) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;

		queryFactory
			.delete(bookMark)
			.where(
				bookMark.feedEntity.schedule.eq(scheduleEntity)
			)
			.execute();
	}

	@Override
	public void deleteByFeedEntity(FeedEntity feedEntity) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;

		queryFactory
			.delete(bookMark)
			.where(
				bookMark.feedEntity.eq(feedEntity)
			)
			.execute();
	}

	@Override
	public BookMarkEntity findByFeedSeqAndMemberSeq(Long feedSeq, Long memberSeq) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(bookMark)
				.where(
					bookMark.feedEntity.feedSeq.eq(feedSeq)
						.and(bookMark.memberEntity.memberSeq.eq(memberSeq))
				).fetchOne()
		).orElseThrow(() -> new BadRequestException(ErrorResult.BOOK_MARK_FEED_NOT_FOUND_EXCEPTION));
	}

	@Override
	public void deleteAllByMemberSeq(MemberEntity memberEntity) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;

		queryFactory.delete(bookMark)
			.where(bookMark.memberEntity.eq(memberEntity))
			.execute();

	}
}

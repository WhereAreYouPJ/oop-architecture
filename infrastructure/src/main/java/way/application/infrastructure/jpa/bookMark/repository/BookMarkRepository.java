package way.application.infrastructure.jpa.bookMark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface BookMarkRepository {
	BookMarkEntity saveBookMarkEntity(BookMarkEntity bookMarkEntity);

	void deleteBookMarkEntity(BookMarkEntity bookMarkEntity);

	void checkBookMarkFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Page<BookMarkEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteByFeedEntity(FeedEntity feedEntity);

	BookMarkEntity findByBookMarkSeq(Long bookMarkSeq);
}

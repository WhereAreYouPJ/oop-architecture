package way.application.infrastructure.bookMark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface BookMarkRepository {
	BookMarkEntity saveBookMarkEntity(BookMarkEntity bookMarkEntity);

	void deleteBookMarkEntity(BookMarkEntity bookMarkEntity);

	void checkBookMarkFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	BookMarkEntity findByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Page<BookMarkEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	);

	boolean isFeedBookMarkedByMember(FeedEntity feedEntity, MemberEntity memberEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteByFeedEntity(FeedEntity feedEntity);
}

package way.application.infrastructure.bookMark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

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
}

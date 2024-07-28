package way.application.infrastructure.bookMark.repository;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

public interface BookMarkRepository {
	BookMarkEntity saveBookMarkEntity(BookMarkEntity bookMarkEntity);
	void checkBookMarkFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

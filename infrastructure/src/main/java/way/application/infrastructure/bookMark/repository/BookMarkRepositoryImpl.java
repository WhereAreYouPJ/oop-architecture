package way.application.infrastructure.bookMark.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository{
	private final BookMarkJpaRepository bookMarkJpaRepository;

	@Override
	public BookMarkEntity saveBookMarkEntity(BookMarkEntity bookMarkEntity) {
		return bookMarkJpaRepository.save(bookMarkEntity);
	}

	@Override
	public void checkBookMarkFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		bookMarkJpaRepository.findBookMarkEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}
}

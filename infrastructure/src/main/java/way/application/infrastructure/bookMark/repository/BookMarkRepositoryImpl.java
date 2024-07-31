package way.application.infrastructure.bookMark.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository {
	private final BookMarkJpaRepository bookMarkJpaRepository;

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
				throw new ConflictException(ErrorResult.HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public BookMarkEntity findByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		return bookMarkJpaRepository.findByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		return bookMarkJpaRepository.existsByFeedEntityAndMemberEntity(feedEntity, memberEntity);
	}
}

package way.application.infrastructure.hideFeed.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class HideFeedRepositoryImpl implements HideFeedRepository {
	private final HideFeedJpaRepository hideFeedJpaRepository;

	@Override
	public HideFeedEntity saveHideFeedEntity(HideFeedEntity hideFeedEntity) {
		return hideFeedJpaRepository.save(hideFeedEntity);
	}

	@Override
	public void findHideFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		hideFeedJpaRepository.findHideFeedEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}
}

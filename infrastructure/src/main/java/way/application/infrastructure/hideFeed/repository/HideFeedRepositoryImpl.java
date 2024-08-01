package way.application.infrastructure.hideFeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
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
	public void deleteHideFeedEntity(HideFeedEntity hideFeedEntity) {
		hideFeedJpaRepository.delete(hideFeedEntity);
	}

	@Override
	public void checkHideFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		hideFeedJpaRepository.findHideFeedEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public HideFeedEntity findHideFeedEntityByFeedEntityAndMemberEntity(
		FeedEntity feedEntity,
		MemberEntity memberEntity
	) {
		return hideFeedJpaRepository.findHideFeedEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public Page<HideFeedEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(MemberEntity memberEntity, Pageable pageable) {
		return hideFeedJpaRepository.findAllByMemberEntityOrderByScheduleStartTimeDesc(memberEntity, pageable);
	}
}

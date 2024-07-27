package way.application.infrastructure.hideFeed.repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

public interface HideFeedRepository {
	HideFeedEntity saveHideFeedEntity(HideFeedEntity hideFeedEntity);
	void deleteHideFeedEntity(HideFeedEntity hideFeedEntity);
	void checkHideFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
	HideFeedEntity findHideFeedEntityByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

package way.application.infrastructure.feed.repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);
	FeedEntity findByFeedSeq(Long feedSeq);
	FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq);
	void deleteAllByFeedSeq(Long feedSeq);
}

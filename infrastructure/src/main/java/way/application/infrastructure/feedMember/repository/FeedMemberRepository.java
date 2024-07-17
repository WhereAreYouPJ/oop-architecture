package way.application.infrastructure.feedMember.repository;

import way.application.infrastructure.feedMember.entity.FeedMemberEntity;

public interface FeedMemberRepository {
	FeedMemberEntity saveFeedMemberEntity(FeedMemberEntity feedMemberEntity);
}

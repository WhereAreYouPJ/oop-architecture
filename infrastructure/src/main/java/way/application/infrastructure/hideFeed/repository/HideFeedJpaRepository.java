package way.application.infrastructure.hideFeed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface HideFeedJpaRepository extends JpaRepository<HideFeedEntity, Long> {
	Optional<HideFeedEntity> findHideFeedEntityByFeedEntityAndMemberEntity(
		FeedEntity feedEntity,
		MemberEntity memberEntity
	);
}

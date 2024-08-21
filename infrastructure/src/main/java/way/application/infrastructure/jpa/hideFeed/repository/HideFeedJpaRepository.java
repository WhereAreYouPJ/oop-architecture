package way.application.infrastructure.jpa.hideFeed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Repository
public interface HideFeedJpaRepository extends JpaRepository<HideFeedEntity, Long> {
	Optional<HideFeedEntity> findHideFeedEntityByFeedEntityAndMemberEntity(
		FeedEntity feedEntity,
		MemberEntity memberEntity
	);
}

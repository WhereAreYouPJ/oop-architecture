package way.application.infrastructure.hideFeed.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Query("""
		SELECT h FROM HideFeedEntity h
		JOIN h.feedEntity f
		JOIN f.schedule s
		WHERE h.memberEntity = :memberEntity
		ORDER BY s.startTime DESC
		""")
	Page<HideFeedEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		@Param("memberEntity") MemberEntity memberEntity,
		Pageable pageable
	);
}

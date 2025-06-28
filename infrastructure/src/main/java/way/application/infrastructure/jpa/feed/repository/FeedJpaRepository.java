package way.application.infrastructure.jpa.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Repository
public interface FeedJpaRepository extends JpaRepository<FeedEntity, Long> {
	Optional<FeedEntity> findFeedEntityByCreatorMemberAndFeedSeq(MemberEntity creatorMember, Long feedSeq);

	void deleteAllByFeedSeq(Long FeedSeq);

	Optional<FeedEntity> findFeedEntityByCreatorMemberAndSchedule(
		MemberEntity creatorMember,
		ScheduleEntity scheduleEntity
	);

	@Query("""
		    SELECT f FROM FeedEntity f
		    LEFT JOIN HideFeedEntity hf ON f.feedSeq = hf.feedEntity.feedSeq
		    WHERE hf.hideFeedSeq IS NULL
		    AND f.schedule.scheduleSeq = :scheduleSeq
		    ORDER BY 
		        CASE WHEN f.creatorMember.memberSeq = :memberSeq THEN 1 ELSE 2 END,
		        FUNCTION('RAND')
		""")
	List<FeedEntity> findFeedByScheduleExcludingHiddenRand(
		@Param("scheduleSeq") Long scheduleSeq,
		@Param("memberSeq") Long memberSeq
	);

	@Query("""
		    SELECT f FROM FeedEntity f
		    LEFT JOIN HideFeedEntity hf ON f.feedSeq = hf.feedEntity.feedSeq
		    WHERE f.schedule.scheduleSeq = :scheduleSeq
		    AND hf.feedEntity.feedSeq IS NULL
		    AND f.creatorMember.memberSeq <> :excludedMemberSeq
		""")
	List<FeedEntity> findFeedByScheduleExcludingHiddenAndCreator(
		@Param("scheduleSeq") Long scheduleSeq,
		@Param("excludedMemberSeq") Long excludedMemberSeq
	);
}

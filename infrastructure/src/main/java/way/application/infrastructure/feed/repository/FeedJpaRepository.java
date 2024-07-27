package way.application.infrastructure.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Repository
public interface FeedJpaRepository extends JpaRepository<FeedEntity, Long> {
	Optional<FeedEntity> findFeedEntityByCreatorMemberAndFeedSeq(MemberEntity creatorMember, Long feedSeq);

	void deleteAllByFeedSeq(Long FeedSeq);

	Optional<FeedEntity> findFeedEntityByCreatorMemberAndSchedule(
		MemberEntity creatorMember,
		ScheduleEntity scheduleEntity
	);
}

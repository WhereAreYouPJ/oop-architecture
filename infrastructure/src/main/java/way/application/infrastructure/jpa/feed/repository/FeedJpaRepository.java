package way.application.infrastructure.jpa.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
}

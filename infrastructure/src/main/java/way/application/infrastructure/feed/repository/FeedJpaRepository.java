package way.application.infrastructure.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Repository
public interface FeedJpaRepository extends JpaRepository<FeedEntity, Long> {
	Optional<FeedEntity> findFeedEntityByCreatorMemberAndFeedSeq(MemberEntity creatorMember, Long feedSeq);
	void deleteAllByFeedSeq(Long FeedSeq);
	List<FeedEntity> findFeedEntitiesBySchedule(ScheduleEntity scheduleEntity);
}

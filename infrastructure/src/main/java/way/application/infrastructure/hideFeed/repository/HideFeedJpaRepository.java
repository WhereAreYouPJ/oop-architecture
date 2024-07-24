package way.application.infrastructure.hideFeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Repository
public interface HideFeedJpaRepository extends JpaRepository<HideFeedEntity, Long> {
	void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

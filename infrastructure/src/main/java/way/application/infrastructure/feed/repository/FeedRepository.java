package way.application.infrastructure.feed.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);

	FeedEntity findByFeedSeq(Long feedSeq);

	FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq);

	void findByCreatorMemberAndSchedule(MemberEntity creatorMemberEntity, ScheduleEntity scheduleEntity);

	void deleteAllByFeedSeq(Long feedSeq);

	Page<FeedEntity> findByScheduleExcludingHidden(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity,
		Pageable pageable
	);

	void deleteFeedEntity(FeedEntity feedEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	Optional<FeedEntity> findByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

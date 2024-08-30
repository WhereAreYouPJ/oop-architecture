package way.application.infrastructure.jpa.feed.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface FeedRepository {
	FeedEntity saveFeedEntity(FeedEntity feedEntity);

	FeedEntity findByFeedSeq(Long feedSeq);

	FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq);

	void findByCreatorMemberAndSchedule(MemberEntity creatorMemberEntity, ScheduleEntity scheduleEntity);

	void deleteAllByFeedSeq(Long feedSeq);

	FeedEntity findByScheduleExcludingHiddenRand(
		ScheduleEntity scheduleEntity,
		MemberEntity memberEntity
	);

	void deleteFeedEntity(FeedEntity feedEntity);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	Optional<FeedEntity> findByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

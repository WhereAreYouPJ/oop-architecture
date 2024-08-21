package way.application.infrastructure.jpa.hideFeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface HideFeedRepository {
	HideFeedEntity saveHideFeedEntity(HideFeedEntity hideFeedEntity);

	void deleteHideFeedEntity(HideFeedEntity hideFeedEntity);

	void verifyHideFeedNotExists(FeedEntity feedEntity, MemberEntity memberEntity);

	Page<HideFeedEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);

	void deleteByFeedEntity(FeedEntity feedEntity);

	HideFeedEntity findByHideFeedSeq(Long hideFeedSeq);
}

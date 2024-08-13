package way.application.infrastructure.hideFeed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface HideFeedRepository {
	HideFeedEntity saveHideFeedEntity(HideFeedEntity hideFeedEntity);

	void deleteHideFeedEntity(HideFeedEntity hideFeedEntity);

	void verifyHideFeedNotExists(FeedEntity feedEntity, MemberEntity memberEntity);

	HideFeedEntity findHideFeedEntityByFeedAndMember(FeedEntity feedEntity, MemberEntity memberEntity);

	Page<HideFeedEntity> findAllByMemberEntityOrderByScheduleStartTimeDesc(
		MemberEntity memberEntity,
		Pageable pageable
	);

	void deleteByScheduleEntity(ScheduleEntity scheduleEntity);
}

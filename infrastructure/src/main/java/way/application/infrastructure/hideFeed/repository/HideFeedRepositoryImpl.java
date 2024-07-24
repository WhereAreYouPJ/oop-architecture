package way.application.infrastructure.hideFeed.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Component
@RequiredArgsConstructor
public class HideFeedRepositoryImpl implements HideFeedRepository {
	private final HideFeedJpaRepository hideFeedJpaRepository;

	@Override
	public void save(HideFeedEntity hideFeedEntity) {
		hideFeedJpaRepository.save(hideFeedEntity);
	}

	@Override
	public void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity) {
		hideFeedJpaRepository.deleteAllByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
	}

}

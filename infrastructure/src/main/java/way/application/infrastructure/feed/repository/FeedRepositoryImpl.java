package way.application.infrastructure.feed.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepository {
	private final FeedJpaRepository feedJpaRepository;

	@Override
	public FeedEntity saveFeedEntity(FeedEntity feedEntity) {
		return feedJpaRepository.save(feedEntity);
	}

	@Override
	public FeedEntity findByFeedSeq(Long feedSeq) {
		return feedJpaRepository.findById(feedSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public FeedEntity findByCreatorMemberAndFeedSeq(MemberEntity creatorMemberEntity, Long feedSeq) {
		return feedJpaRepository.findFeedEntityByCreatorMemberAndFeedSeq(creatorMemberEntity, feedSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public void findByCreatorMemberAndSchedule(MemberEntity creatorMemberEntity, ScheduleEntity scheduleEntity) {
		feedJpaRepository.findFeedEntityByCreatorMemberAndSchedule(creatorMemberEntity, scheduleEntity)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.FEED_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public void deleteAllByFeedSeq(Long feedSeq) {
		feedJpaRepository.deleteAllByFeedSeq(feedSeq);
	}
}

package way.application.infrastructure.feedMember.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feedMember.entity.FeedMemberEntity;

@Component
@RequiredArgsConstructor
public class FeedMemberRepositoryImpl implements FeedMemberRepository {
	private final FeedMemberJpaRepository feedMemberJpaRepository;

	@Override
	public FeedMemberEntity saveFeedMemberEntity(FeedMemberEntity feedMemberEntity) {
		return feedMemberJpaRepository.save(feedMemberEntity);
	}
}

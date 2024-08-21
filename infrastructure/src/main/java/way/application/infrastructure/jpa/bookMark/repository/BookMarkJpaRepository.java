package way.application.infrastructure.jpa.bookMark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Repository
public interface BookMarkJpaRepository extends JpaRepository<BookMarkEntity, Long> {
	Optional<BookMarkEntity> findBookMarkEntityByFeedEntityAndMemberEntity(
		FeedEntity feedEntity,
		MemberEntity memberEntity
	);

	Optional<BookMarkEntity> findByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

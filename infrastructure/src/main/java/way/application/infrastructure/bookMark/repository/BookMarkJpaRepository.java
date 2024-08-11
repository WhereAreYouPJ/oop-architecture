package way.application.infrastructure.bookMark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface BookMarkJpaRepository extends JpaRepository<BookMarkEntity, Long> {
	Optional<BookMarkEntity> findBookMarkEntityByFeedEntityAndMemberEntity(
		FeedEntity feedEntity,
		MemberEntity memberEntity
	);

	Optional<BookMarkEntity> findByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	Boolean existsByFeedEntityAndMemberEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}

package way.application.infrastructure.feedMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feedMember.entity.FeedMemberEntity;

@Repository
public interface FeedMemberJpaRepository extends JpaRepository<FeedMemberEntity, Long> {

}

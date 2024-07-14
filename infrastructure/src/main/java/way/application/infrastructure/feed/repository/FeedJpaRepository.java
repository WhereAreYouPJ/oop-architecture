package way.application.infrastructure.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feed.entity.FeedEntity;

@Repository
public interface FeedJpaRepository extends JpaRepository<FeedEntity, Long> {

}

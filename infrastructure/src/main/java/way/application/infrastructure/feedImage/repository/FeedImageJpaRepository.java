package way.application.infrastructure.feedImage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.feedImage.entity.FeedImageEntity;

@Repository
public interface FeedImageJpaRepository extends JpaRepository<FeedImageEntity, Long> {

}

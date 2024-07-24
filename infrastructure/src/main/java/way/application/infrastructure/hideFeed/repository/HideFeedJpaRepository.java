package way.application.infrastructure.hideFeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.hideFeed.entity.HideFeedEntity;

@Repository
public interface HideFeedJpaRepository extends JpaRepository<HideFeedEntity, Long> {

}

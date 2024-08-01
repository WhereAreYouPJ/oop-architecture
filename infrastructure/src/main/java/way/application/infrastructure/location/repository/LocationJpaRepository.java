package way.application.infrastructure.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.location.entity.LocationEntity;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
}

package way.application.infrastructure.jpa.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.location.entity.LocationEntity;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {

}

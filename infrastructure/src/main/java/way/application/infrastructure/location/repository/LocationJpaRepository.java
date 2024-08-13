package way.application.infrastructure.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {

}

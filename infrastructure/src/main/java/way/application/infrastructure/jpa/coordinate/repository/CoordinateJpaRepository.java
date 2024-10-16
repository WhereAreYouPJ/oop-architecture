package way.application.infrastructure.jpa.coordinate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Repository
public interface CoordinateJpaRepository extends JpaRepository<CoordinateEntity, Long> {
	Optional<CoordinateEntity> findByMemberEntity(MemberEntity memberEntity);
}

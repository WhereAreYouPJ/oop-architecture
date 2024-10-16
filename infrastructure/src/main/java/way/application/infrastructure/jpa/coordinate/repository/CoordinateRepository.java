package way.application.infrastructure.jpa.coordinate.repository;

import java.util.Optional;

import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

public interface CoordinateRepository {
	CoordinateEntity saveCoordinateEntity(CoordinateEntity coordinateEntity);

	Optional<CoordinateEntity> findOptionalCoordinateByMemberEntity(MemberEntity memberEntity);

	CoordinateEntity findByMemberEntity(MemberEntity memberEntity);
}

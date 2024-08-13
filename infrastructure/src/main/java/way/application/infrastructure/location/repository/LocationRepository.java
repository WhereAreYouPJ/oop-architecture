package way.application.infrastructure.location.repository;

import java.util.List;

import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.member.entity.MemberEntity;

public interface LocationRepository {
	LocationEntity saveLocationEntity(LocationEntity locationEntity);

	List<LocationEntity> findAllByMemberEntityAndLocationSeqs(MemberEntity memberEntity, List<Long> locationSeqs);

	void deleteAll(List<LocationEntity> locationEntities);

	List<LocationEntity> findByMemberEntity(MemberEntity memberEntity);
}

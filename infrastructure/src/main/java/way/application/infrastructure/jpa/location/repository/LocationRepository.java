package way.application.infrastructure.jpa.location.repository;

import java.util.List;

import way.application.infrastructure.jpa.location.entity.LocationEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

public interface LocationRepository {
	LocationEntity saveLocationEntity(LocationEntity locationEntity);

	List<LocationEntity> findAllByMemberEntityAndLocationSeqs(MemberEntity memberEntity, List<Long> locationSeqs);

	void deleteAll(List<LocationEntity> locationEntities);

	List<LocationEntity> findByMemberEntity(MemberEntity memberEntity);

    void deleteAllByMemberSeq(MemberEntity memberEntity);
}

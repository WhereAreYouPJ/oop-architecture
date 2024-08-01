package way.application.infrastructure.location.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
	private final LocationJpaRepository locationJpaRepository;

	@Override
	public LocationEntity saveLocationEntity(LocationEntity locationEntity) {
		return locationJpaRepository.save(locationEntity);
	}

	@Override
	public List<LocationEntity> findAllByMemberEntityAndLocationSeqs(
		MemberEntity memberEntity,
		List<Long> locationSeqs
	) {
		List<Long> locationSeqsByMemberEntity = locationJpaRepository.findLocationSeqsByMemberEntity(memberEntity);
		List<Long> locationSeqsByLocationSeqs = locationJpaRepository.findLocationSeqsByLocationSeqs(locationSeqs);

		if (locationSeqsByMemberEntity.size() != locationSeqsByLocationSeqs.size()) {
			throw new BadRequestException(ErrorResult.LOCATION_SEQ_BAD_REQUEST_EXCEPTION);
		}

		return locationJpaRepository.findAllByMemberEntityAndLocationSeqs(memberEntity, locationSeqs);
	}

	@Override
	public void deleteAll(List<LocationEntity> locationEntities) {
		locationJpaRepository.deleteAll(locationEntities);
	}
}

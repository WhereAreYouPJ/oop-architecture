package way.application.infrastructure.location.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.location.entity.LocationEntity;

@Component
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
	private final LocationJpaRepository locationJpaRepository;

	@Override
	public LocationEntity saveLocationEntity(LocationEntity locationEntity) {
		return locationJpaRepository.save(locationEntity);
	}
}

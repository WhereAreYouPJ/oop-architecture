package way.application.infrastructure.location.repository;

import way.application.infrastructure.location.entity.LocationEntity;

public interface LocationRepository {
	LocationEntity saveLocationEntity(LocationEntity locationEntity);
}

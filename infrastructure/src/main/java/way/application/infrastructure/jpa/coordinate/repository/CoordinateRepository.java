package way.application.infrastructure.jpa.coordinate.repository;

import java.util.Optional;

import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface CoordinateRepository {
	CoordinateEntity saveCoordinateEntity(CoordinateEntity coordinateEntity);

	Optional<CoordinateEntity> findOptionalCoordinateByMemberEntity(
		MemberEntity memberEntity,
		ScheduleEntity scheduleEntity
	);

	CoordinateEntity findByMemberEntity(MemberEntity memberEntity, ScheduleEntity scheduleEntity);
}

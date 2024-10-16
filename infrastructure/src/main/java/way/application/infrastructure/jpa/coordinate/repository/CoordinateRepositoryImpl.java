package way.application.infrastructure.jpa.coordinate.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class CoordinateRepositoryImpl implements CoordinateRepository {
	private final CoordinateJpaRepository coordinateJpaRepository;

	@Override
	public CoordinateEntity saveCoordinateEntity(CoordinateEntity coordinateEntity) {
		return coordinateJpaRepository.save(coordinateEntity);
	}

	@Override
	public Optional<CoordinateEntity> findOptionalCoordinateByMemberEntity(MemberEntity memberEntity) {
		return coordinateJpaRepository.findByMemberEntity(memberEntity);
	}

	@Override
	public CoordinateEntity findByMemberEntity(MemberEntity memberEntity) {
		return coordinateJpaRepository.findByMemberEntity(memberEntity)
			.orElseThrow(() -> new BadRequestException(ErrorResult.COORDINATE_NOT_FOUND_EXCEPTION));
	}
}

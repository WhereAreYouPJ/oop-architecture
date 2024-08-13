package way.application.infrastructure.location.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.location.entity.QLocationEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
	private final LocationJpaRepository locationJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public LocationEntity saveLocationEntity(LocationEntity locationEntity) {
		return locationJpaRepository.save(locationEntity);
	}

	@Override
	public List<LocationEntity> findAllByMemberEntityAndLocationSeqs(
		MemberEntity memberEntity,
		List<Long> locationSeqs
	) {
		QLocationEntity location = QLocationEntity.locationEntity;

		return queryFactory
			.selectFrom(location)
			.where(
				location.memberEntity.eq(memberEntity)
					.and(location.locationSeq.in(locationSeqs))
			)
			.fetch();
	}

	@Override
	public void deleteAll(List<LocationEntity> locationEntities) {
		locationJpaRepository.deleteAll(locationEntities);
	}

	@Override
	public List<LocationEntity> findByMemberEntity(MemberEntity memberEntity) {
		QLocationEntity location = QLocationEntity.locationEntity;

		return queryFactory
			.selectFrom(location)
			.where(
				location.memberEntity.eq(memberEntity)
			)
			.fetch();
	}
}

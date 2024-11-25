package way.application.infrastructure.jpa.location.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.location.entity.LocationEntity;
import way.application.infrastructure.jpa.location.entity.QLocationEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
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

		List<LocationEntity> locationEntities = queryFactory
			.selectFrom(location)
			.where(
				location.memberEntity.eq(memberEntity)
					.and(location.locationSeq.in(locationSeqs))
			)
			.fetch();

		if (locationEntities.size() != locationSeqs.size()) {
			throw new BadRequestException(ErrorResult.LOCATION_SEQ_BAD_REQUEST_EXCEPTION);
		}

		return locationEntities;
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
			.orderBy(location.sequence.asc())
			.fetch();
	}

	@Override
	public void deleteAllByMemberSeq(MemberEntity memberEntity) {
		QLocationEntity location = QLocationEntity.locationEntity;

		queryFactory.delete(location)
			.where(
				location.memberEntity.eq(memberEntity)
			)
			.execute();

	}

	@Override
	public Long findMaxSequenceByMemberEntity(MemberEntity memberEntity) {
		QLocationEntity location = QLocationEntity.locationEntity;

		List<LocationEntity> results = queryFactory
			.selectFrom(location)
			.where(
				location.memberEntity.eq(memberEntity)
			)
			.fetch();

		// 만약 존재한다면 가장 큰 sequence 값을 반환하고, 없다면 0L을 반환
		return results.isEmpty() ? 0L : results.stream()
			.map(LocationEntity::getSequence)
			.max(Long::compare)
			.orElse(0L);  // 가장 큰 sequence 값 반환
	}

	@Override
	public void saveAll(List<LocationEntity> locationEntities) {
		locationJpaRepository.saveAll(locationEntities);
	}
}

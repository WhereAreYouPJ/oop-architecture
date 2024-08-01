package way.application.infrastructure.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
	@Query("""
		SELECT 
			l.locationSeq
		FROM 
			LocationEntity l
		WHERE
			l.memberEntity = :memberEntity
		""")
	List<Long> findLocationSeqsByMemberEntity(MemberEntity memberEntity);

	@Query("""
        SELECT 
            l.locationSeq
        FROM 
            LocationEntity l
        WHERE
            l.locationSeq IN :locationSeqs
        """)
	List<Long> findLocationSeqsByLocationSeqs(List<Long> locationSeqs);

	@Query("""
		SELECT 
			l
		FROM 
			LocationEntity l
		WHERE 
			l.memberEntity = :memberEntity 
			AND 
			l.locationSeq IN :locationSeqs
		""")
	List<LocationEntity> findAllByMemberEntityAndLocationSeqs(MemberEntity memberEntity, List<Long> locationSeqs);
}

package way.application.infrastructure.scheduleMember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Repository
public interface ScheduleMemberJpaRepository extends JpaRepository<ScheduleMemberEntity, Long> {
	@Query("""
		SELECT 
			sme 
		FROM 
			ScheduleMemberEntity sme 
		WHERE 
			sme.schedule.scheduleSeq =:scheduleSeq 
			AND 
			sme.invitedMember.memberSeq =:memberSeq 
			AND 
			sme.acceptSchedule = true
			AND
			sme.isCreator = true
		""")
	Optional<ScheduleMemberEntity> findScheduleMemberEntityByCreatorAndSchedule(
		@Param("scheduleSeq") Long scheduleSeq,
		@Param("memberSeq") Long memberSeq
	);

	void deleteAllBySchedule(ScheduleEntity scheduleEntity);

	@Query("""
		SELECT 
			sme 
		FROM 
			ScheduleMemberEntity sme 
		WHERE 
			sme.schedule.scheduleSeq =:scheduleSeq 
			AND 
			sme.invitedMember.memberSeq =:memberSeq 
			AND 
			sme.acceptSchedule = true
		""")
	Optional<ScheduleMemberEntity> findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
		@Param("scheduleSeq") Long scheduleSeq,
		@Param("memberSeq") Long memberSeq
	);

	@Query("""
		SELECT 
			sme 
		FROM 
			ScheduleMemberEntity sme 
		WHERE 
			sme.schedule =:scheduleEntity 
			AND 
			sme.acceptSchedule = true
		""")
	List<ScheduleMemberEntity> findAcceptedScheduleMemberByScheduleEntity(
		@Param("scheduleEntity") ScheduleEntity scheduleEntity
	);

	@Query("""
			SELECT 
				sme
			FROM 
				ScheduleMemberEntity sme
			WHERE 
				sme.invitedMember.memberSeq =:memberSeq
				AND
				sme.schedule.scheduleSeq =:scheduleSeq
		""")
	Optional<ScheduleMemberEntity> findScheduleMemberEntityByMemberSeqAndScheduleSeq(
		@Param("memberSeq") Long memberSeq,
		@Param("scheduleSeq") Long scheduleSeq
	);
}

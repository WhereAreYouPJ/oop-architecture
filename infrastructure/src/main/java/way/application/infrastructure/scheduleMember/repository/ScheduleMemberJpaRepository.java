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
	Optional<ScheduleMemberEntity> findByScheduleSeqAndMemberSeqAndAcceptScheduleTrueAndIsCreatorTrue(
		@Param("scheduleSeq") Long scheduleSeq,
		@Param("memberSeq") Long memberSeq
	);

	void deleteAllBySchedule(ScheduleEntity scheduleEntity);
}

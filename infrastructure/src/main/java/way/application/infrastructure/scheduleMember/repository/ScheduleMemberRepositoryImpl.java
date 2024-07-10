package way.application.infrastructure.scheduleMember.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ScheduleMemberRepositoryImpl implements ScheduleMemberRepository {
	private final ScheduleMemberJpaRepository scheduleMemberJpaRepository;

	@Override
	public ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity) {
		return scheduleMemberJpaRepository.save(scheduleMemberEntity);
	}

	@Override
	public ScheduleEntity validateScheduleEntityCreatedByMember(Long scheduleSeq, Long memberSeq) {
		return scheduleMemberJpaRepository.findScheduleMemberEntityByCreatorAndSchedule(
			scheduleSeq, memberSeq
		).orElseThrow(() ->
			new BadRequestException(ErrorResult.SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION)).getSchedule();
	}

	@Override
	public void deleteAllBySchedule(ScheduleEntity scheduleEntity) {
		scheduleMemberJpaRepository.deleteAllBySchedule(scheduleEntity);
	}

	@Override
	public ScheduleMemberEntity findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
		Long scheduleSeq,
		Long memberSeq
	) {
		return scheduleMemberJpaRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(scheduleSeq, memberSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<ScheduleMemberEntity> findAcceptedScheduleMemberByScheduleEntity(ScheduleEntity scheduleEntity) {
		return scheduleMemberJpaRepository.findAcceptedScheduleMemberByScheduleEntity(scheduleEntity);
	}

}

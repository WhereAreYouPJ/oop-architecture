package way.application.infrastructure.jpa.scheduleMember.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.QScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class ScheduleMemberRepositoryImpl implements ScheduleMemberRepository {
	private final ScheduleMemberJpaRepository scheduleMemberJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity) {
		return scheduleMemberJpaRepository.save(scheduleMemberEntity);
	}

	@Override
	public ScheduleEntity findScheduleIfCreatedByMember(Long scheduleSeq, Long memberSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.select(scheduleMember.schedule)
				.from(scheduleMember)
				.where(
					scheduleMember.schedule.scheduleSeq.eq(scheduleSeq)
						.and(scheduleMember.invitedMember.memberSeq.eq(memberSeq))
						.and(scheduleMember.acceptSchedule.isTrue())
						.and(scheduleMember.isCreator.isTrue())
				)
				.fetchOne())
			.orElseThrow(
				() -> new BadRequestException(ErrorResult.SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION)
			);
	}

	@Override
	public List<ScheduleEntity> findSchedulesIfCreatedByMember(MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.select(scheduleMember.schedule)
			.from(scheduleMember)
			.where(
				scheduleMember.isCreator.isTrue()
					.and(scheduleMember.invitedMember.eq(memberEntity))
			)
			.fetch();
	}

	@Override
	public void deleteAllByMemberSeq(MemberEntity memberEntity, List<ScheduleEntity> scheduleEntities) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.in(scheduleEntities)
			).execute();

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
			)
			.execute();
	}

	@Override
	public List<ScheduleMemberEntity> findAllByScheduleEntity(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
			).fetch();
	}

	@Override
	public void deleteRemainScheduleEntity(ScheduleEntity scheduleEntity, List<MemberEntity> memberEntities) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
					.and(scheduleMember.invitedMember.notIn(memberEntities))
			).execute();
	}

	@Override
	public void deleteAllBySchedule(ScheduleEntity scheduleEntity) {
		scheduleMemberJpaRepository.deleteAllBySchedule(scheduleEntity);
	}

	@Override
	public ScheduleMemberEntity findAcceptedScheduleMemberInSchedule(Long scheduleSeq, Long memberSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.selectFrom(scheduleMember)
				.where(
					scheduleMember.schedule.scheduleSeq.eq(scheduleSeq)
						.and(scheduleMember.invitedMember.memberSeq.eq(memberSeq))
						.and(scheduleMember.acceptSchedule.isTrue())
				)
				.fetchOne())
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<ScheduleMemberEntity> findAllAcceptedScheduleMembersInSchedule(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
			)
			.fetch();
	}

	@Override
	public List<ScheduleMemberEntity> findAllAcceptedScheduleMembersFriendsInSchedule(ScheduleEntity scheduleEntity,
		MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.fetch();
	}

	@Override
	public ScheduleMemberEntity findScheduleMemberInSchedule(Long memberSeq, Long scheduleSeq) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return Optional.ofNullable(queryFactory
				.selectFrom(scheduleMember)
				.where(
					scheduleMember.invitedMember.memberSeq.eq(memberSeq)
						.and(scheduleMember.schedule.scheduleSeq.eq(scheduleSeq))
				)
				.fetchOne())
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<ScheduleMemberEntity> findByMemberEntity(MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;

		// QueryDSL을 사용하여 ScheduleMemberEntity를 조회
		return queryFactory
			.selectFrom(scheduleMember)
			.join(scheduleMember.schedule, schedule).fetchJoin()
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.fetch();
	}


	@Override
	public void deleteByScheduleEntity(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
			)
			.execute();
	}

	@Override
	public void deleteByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		queryFactory
			.delete(scheduleMember)
			.where(
				scheduleMember.schedule.eq(scheduleEntity)
					.and(scheduleMember.invitedMember.eq(memberEntity))
			)
			.execute();
	}

	@Override
	public void validateScheduleMemberIsCreator(MemberEntity memberEntity, ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		Optional.ofNullable(queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.schedule.eq(scheduleEntity))
					.and(scheduleMember.isCreator.isTrue())
			)
			.fetchOne()
		).ifPresent(entity -> {
			throw new BadRequestException(ErrorResult.MEMBER_CREATED_SCHEDULE_BAD_REQUEST_EXCEPTION);
		});
	}

	@Override
	public void validateScheduleMemberAccept(MemberEntity memberEntity, ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		Optional.ofNullable(queryFactory
			.selectFrom(scheduleMember)
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.schedule.eq(scheduleEntity))
					.and(scheduleMember.acceptSchedule.isTrue())
			)
			.fetchOne()
		).ifPresent(entity -> {
			throw new BadRequestException(ErrorResult.MEMBER_ALREADY_ACCEPT_SCHEDULE_BAD_REQUEST_EXCEPTION);
		});
	}

	@Override
	public List<ScheduleMemberEntity> findInvitedScheduleMemberEntity(MemberEntity memberEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;
		QScheduleEntity schedule = QScheduleEntity.scheduleEntity;

		return queryFactory
			.selectFrom(scheduleMember)
			.join(scheduleMember.schedule, schedule)
			.where(
				scheduleMember.invitedMember.eq(memberEntity)
					.and(scheduleMember.acceptSchedule.isFalse())
					.and(schedule.startTime.goe(LocalDate.now().atStartOfDay()))
			).fetch();
	}

	@Override
	public Long findCreatorBySchedule(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
			.select(scheduleMember.invitedMember.memberSeq)
			.from(scheduleMember)
			.where(
				scheduleMember.isCreator.isTrue()
					.and(scheduleMember.schedule.scheduleSeq.eq(scheduleEntity.getScheduleSeq()))
			).fetchOne();
	}

	@Override
	public String findCreatorNameBySchedule(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity scheduleMember = QScheduleMemberEntity.scheduleMemberEntity;

		return queryFactory
				.select(scheduleMember.invitedMember.userName)
				.from(scheduleMember)
				.where(
						scheduleMember.isCreator.isTrue()
								.and(scheduleMember.schedule.scheduleSeq.eq(scheduleEntity.getScheduleSeq()))
				).fetchOne();
	}
}

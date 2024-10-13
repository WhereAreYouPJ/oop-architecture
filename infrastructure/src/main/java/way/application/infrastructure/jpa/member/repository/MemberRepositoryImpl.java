package way.application.infrastructure.jpa.member.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.bookMark.entity.QBookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.entity.QFeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.entity.QMemberEntity;
import way.application.infrastructure.jpa.schedule.entity.QScheduleEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.scheduleMember.entity.QScheduleMemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
	private final MemberJpaRepository memberJpaRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final JPAQueryFactory queryFactory;
	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;

	@Override
	public MemberEntity findByMemberSeq(Long memberSeq) {
		return memberJpaRepository.findById(memberSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public List<MemberEntity> findByMemberSeqs(List<Long> memberSeqs) {
		if (memberSeqs == null || memberSeqs.isEmpty()) {
			return Collections.emptyList();
		}

		List<MemberEntity> memberEntities = memberJpaRepository.findAllById(memberSeqs);
		if (memberEntities.size() != memberSeqs.size()) {
			throw new BadRequestException(ErrorResult.MEMBER_SEQ_BAD_REQUEST_EXCEPTION);
		}

		return memberEntities;
	}

	@Override
	public MemberEntity saveMember(MemberEntity memberEntity) {
		return memberJpaRepository.save(memberEntity);
	}

	@Override
	public void isDuplicatedEmail(String email) {
		memberJpaRepository.findByEmail(email)
			.ifPresent(entity -> {
				throw new ConflictException(ErrorResult.EMAIL_DUPLICATION_CONFLICT_EXCEPTION);
			});
	}

	@Override
	public MemberEntity validateEmail(String email) {
		return memberJpaRepository.findByEmail(email)
			.orElseThrow(() -> new BadRequestException(ErrorResult.EMAIL_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public void saveRefreshToken(String refreshToken, String email) {

		redisTemplate.opsForValue()
			.set(
				email,
				refreshToken,
				refreshTokenExpiration,
				TimeUnit.MILLISECONDS
			);
	}

	@Override
	public void saveAuthKey(String email, String authKey) {
		redisTemplate.opsForValue().set(
			email,
			authKey,
			300000,
			TimeUnit.MILLISECONDS
		);
	}

	@Override
	public String getCode(String email) {
		return redisTemplate.opsForValue().get(email);
	}

	@Override
	public void deleteCode(String email) {
		redisTemplate.delete(email);
	}

	@Override
	public void deleteJwt(String email) {
		redisTemplate.delete(email);
	}

	@Override
	public MemberEntity findByMemberCode(String memberCode) {

		return memberJpaRepository.findByMemberCode(memberCode)
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_CODE_BAD_REQUEST_EXCEPTION));

	}

	@Override
	public List<MemberEntity> findByFeedEntity(FeedEntity feedEntity, MemberEntity memberEntity) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;
		QScheduleEntity scheduleEntity = QScheduleEntity.scheduleEntity;
		QScheduleMemberEntity scheduleMemberEntity = QScheduleMemberEntity.scheduleMemberEntity;
		QMemberEntity member = QMemberEntity.memberEntity;
		QFeedEntity feed = QFeedEntity.feedEntity;

		return queryFactory.select(member)
			.from(member)
			.join(scheduleMemberEntity).on(scheduleMemberEntity.invitedMember.memberSeq.eq(member.memberSeq))
			.join(scheduleEntity).on(scheduleEntity.scheduleSeq.eq(scheduleMemberEntity.schedule.scheduleSeq))
			.join(feed).on(scheduleEntity.scheduleSeq.eq(feedEntity.getSchedule().getScheduleSeq()))
			.join(bookMark).on(bookMark.feedEntity.feedSeq.eq(feedEntity.getFeedSeq()))
			.where(scheduleMemberEntity.invitedMember.ne(memberEntity))
			.fetch();

	}

	@Override
	public MemberEntity findByEmail(String email) {
		Optional<MemberEntity> byEmail = memberJpaRepository.findByEmail(email);

		return byEmail.orElseThrow(() -> new BadRequestException(ErrorResult.EMAIL_BAD_REQUEST_EXCEPTION));

	}

	@Override
	public void deleteByMemberSeq(MemberEntity memberEntity) {
		memberJpaRepository.delete(memberEntity);
	}

	@Override
	public List<MemberEntity> findByScheduleEntityAcceptTrue(ScheduleEntity scheduleEntity) {
		QScheduleMemberEntity qScheduleMemberEntity = QScheduleMemberEntity.scheduleMemberEntity;
		QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

		return queryFactory
			.select(qMemberEntity)
			.from(qScheduleMemberEntity)
			.join(qScheduleMemberEntity.invitedMember, qMemberEntity)
			.where(
				qScheduleMemberEntity.schedule.eq(scheduleEntity)
					.and(qScheduleMemberEntity.acceptSchedule.isTrue())
			)
			.fetch();
	}
}

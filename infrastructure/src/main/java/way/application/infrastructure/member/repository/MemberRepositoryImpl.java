package way.application.infrastructure.member.repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ConflictException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
	private final MemberJpaRepository memberJpaRepository;
	private final RedisTemplate<String, String> redisTemplate;
	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;


	@Override
	public MemberEntity findByMemberSeq(Long memberSeq) {
		return memberJpaRepository.findById(memberSeq)
			.orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_BAD_REQUEST_EXCEPTION));
	}

	@Override
	public void isDuplicatedUserId(String userId) {
		memberJpaRepository.findByUserId(userId)
				.ifPresent(user -> {
					throw new ConflictException(ErrorResult.USER_ID_DUPLICATION_CONFLICT_EXCEPTION);
				});
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
}

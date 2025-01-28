package way.application.domain.member;

import java.nio.charset.StandardCharsets;
import java.util.*;

import io.jsonwebtoken.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.application.utils.exception.NotFoundRequestException;
import way.application.utils.exception.UnauthorizedException;

@Component
@RequiredArgsConstructor
public class MemberDomain {

	private final BCryptPasswordEncoder encoder;
	private final JavaMailSender javaMailSender;
	private final RedisTemplate<String, String> redisTemplate;
	private final TemplateEngine templateEngine;
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.accessTokenExpiration}")
	private long accessTokenExpiration;
	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;

	/**
	 * @param createMemberEntity
	 * @param invitedMemberEntity
	 *
	 * 일정 추가 시 일정에 들어가는 MemberEntity 구성
	 * create(일정 생성자) + invited(일정 초대자) Set 구성 후 반환
	 */
	public Set<MemberEntity> createMemberSet(MemberEntity createMemberEntity, List<MemberEntity> invitedMemberEntity) {
		Set<MemberEntity> totalMemberEntity = new HashSet<>(invitedMemberEntity);
		totalMemberEntity.add(createMemberEntity);

		return totalMemberEntity;
	}

	/**
	 * @param invitedMemberEntity
	 * @param createMemberEntity
	 *
	 * 일정 초대자 <-> 일정 생성자 동일 여부 확인
	 * Seq 값 비교 처리
	 */
	public boolean checkIsCreator(MemberEntity invitedMemberEntity, MemberEntity createMemberEntity) {
		return invitedMemberEntity.getMemberSeq().equals(createMemberEntity.getMemberSeq());
	}

	public void checkPassword(String password, String encodedPassword) {

		if(!encoder.matches(password, encodedPassword)) {
			throw new BadRequestException(ErrorResult.PASSWORD_BAD_REQUEST_EXCEPTION);
		}

	}

	public String generateAccessToken(Long memberSeq) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
				.setSubject(String.valueOf(memberSeq))
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();

	}

	public String generateRefreshToken(Long memberSeq) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
				.setSubject(String.valueOf(memberSeq))
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String generateAuthKey() {
		Random random = new Random();
		return String.valueOf(random.nextInt(888888) + 111111);

	}

	public void sendAuthKey(String email, String authKey) {
		String subject = "지금어디 이메일 인증번호";

		// Thymeleaf 컨텍스트 설정
		Context context = new Context();
		context.setVariable("authKey", authKey);

		// HTML 템플릿 렌더링
		String htmlContent = templateEngine.process("email-template", context);

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(htmlContent, true);

			// 로고 이미지 첨부 (cid를 사용하여 inline 삽입)
			helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("이메일 전송 실패", e);
		}
	}

	public void verifyCode(String code, String verifyCode) {

		if(!code.equals(verifyCode)) {
			throw new BadRequestException(ErrorResult.CODE_BAD_REQUEST_EXCEPTION);
		}
	}

	public void validateResetPassword(String password, String checkPassword) {

		if(!password.equals(checkPassword)) {
			throw new BadRequestException(ErrorResult.PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION);
		}

	}

	public void fireBaseTargetToken(MemberEntity memberEntity) {

		memberEntity.deleteFireBaseTargetToken();
	}

	public String generateMemberCode() {
		StringBuilder sb = new StringBuilder();
		Random rd = new Random();

		for (int i = 0; i < 6; i++) {

			if (rd.nextBoolean()) {
				sb.append(rd.nextInt(10));
			} else {
				sb.append((char) (rd.nextInt(26) + 65));
			}
		}

		return sb.toString();
	}

    public Long extractMemberSeq(String refreshToken) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret)
				.parseClaimsJws(refreshToken)
				.getBody();

		return Long.parseLong(claims.getSubject());
    }

	public Long validateToken(String token) {
		try{
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return extractMemberSeq(token);
		} catch(ExpiredJwtException e) {
			throw new UnauthorizedException(ErrorResult.EXPIRED_TOKEN_UNAUTHORIZED_EXCEPTION);
		} catch(JwtException e) {
			throw new NotFoundRequestException(ErrorResult.TOKEN_NOT_FOUND_EXCEPTION);
		}
	}

	public void validateRefreshToken(Long memberSeq, String refreshToken) {

		String savedRefreshToken = redisTemplate.opsForValue().get(String.valueOf(memberSeq));

		if(savedRefreshToken == null) {
			throw new NotFoundRequestException(ErrorResult.TOKEN_NOT_FOUND_EXCEPTION);
		}

        if(!savedRefreshToken.equals(refreshToken)) {
			throw new BadRequestException(ErrorResult.TOKEN_MISMATCH_BAD_REQUEST_EXCEPTION);
		}
	}
}

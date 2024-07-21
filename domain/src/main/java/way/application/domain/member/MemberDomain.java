package way.application.domain.member;

import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import way.application.infrastructure.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class MemberDomain {

	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.accessTokenExpiration}")
	private long accessTokenExpiration;
	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;
	private final BCryptPasswordEncoder encoder;
	private final JavaMailSender javaMailSender;

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

	public String generateAccessToken(String email) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();

	}

	public String generateRefreshToken(String email) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
				.setSubject(email)
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
		String subject = "지금어디 인증번호 입니다.";
		String text = "인증번호는 " + authKey + "입니다. <br/>";

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(text, true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new BadRequestException(ErrorResult.EMAIL_BAD_REQUEST_EXCEPTION);
		}
	}
}

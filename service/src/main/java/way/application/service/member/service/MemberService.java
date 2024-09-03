package way.application.service.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.domain.member.MemberDomain;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.service.member.dto.request.MemberRequestDto;
import way.application.service.member.dto.request.MemberRequestDto.SaveMemberRequestDto;
import way.application.service.member.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import way.application.utils.s3.S3Utils;

import java.io.IOException;

import static way.application.service.member.dto.request.MemberRequestDto.*;
import static way.application.service.member.dto.response.MemberResponseDto.*;


@Service
@RequiredArgsConstructor
public class MemberService {
	/**
	 * 유효성 검사 -> Repository Interface 에서 처리
	 * 비즈니스 로직 -> Domain 단에서 처리
	 * Service 로직 -> Domain 호출 및 저장
	 *
	 * Service Layer -> Repo의 CRUD만 처리
	 */

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;
	private final BCryptPasswordEncoder encoder;
	private final MemberDomain memberDomain;
	private final S3Utils s3Util;

	@Transactional
	public void saveMember(SaveMemberRequestDto saveMemberRequestDto) {

		//멤버 유효성 검사
		memberRepository.isDuplicatedEmail(saveMemberRequestDto.email());

		String memberCode = memberDomain.generateMemberCode();

		// Member 저장
		memberRepository.saveMember(
				memberMapper.toMemberEntity(saveMemberRequestDto,encoder.encode(saveMemberRequestDto.password()),memberCode)
		);


	}

	public CheckEmailResponseDto checkEmail(CheckEmailRequestDto checkEmailRequestDto) {

		//email 중복 검사
		memberRepository.isDuplicatedEmail(checkEmailRequestDto.email());

		return new CheckEmailResponseDto(checkEmailRequestDto.email());
	}

	public LoginResponseDto login(LoginRequestDto loginRequestDto) {

		// 이메일 검증
		MemberEntity memberEntity = memberRepository.validateEmail(loginRequestDto.email());

		// 비번 검증
		memberDomain.checkPassword(loginRequestDto.password(), memberEntity.getEncodedPassword());

		// jwt 생성
		String accessToken = memberDomain.generateAccessToken(loginRequestDto.email());
		String refreshToken = memberDomain.generateRefreshToken(loginRequestDto.email());

		// refreshToken 저장
		memberRepository.saveRefreshToken(refreshToken, loginRequestDto.email());

		// fcm 저장
		memberEntity.saveFireBaseTargetToken(loginRequestDto.fcmToken());
		memberRepository.saveMember(memberEntity);

		return new LoginResponseDto(accessToken,refreshToken,memberEntity.getMemberSeq(),memberEntity.getMemberCode());
	}

	public void send(MailSendRequestDto mailSendRequestDto) {

		// authKey 생성
		String authKey = memberDomain.generateAuthKey();

		// 코드 발송
		memberDomain.sendAuthKey(mailSendRequestDto.email(), authKey);

		// 인증코드 저장
		memberRepository.saveAuthKey(mailSendRequestDto.email(), authKey);
	}

	public void verify(VerifyCodeDto verifyCodeDto) {

		// 인증코드 조회
		String verifyCode = memberRepository.getCode(verifyCodeDto.email());

		// 인증코드 검사
		memberDomain.verifyCode(verifyCodeDto.code(), verifyCode);

		// 인증코드 삭제
		memberRepository.deleteCode(verifyCodeDto.email());

	}

	public void verifyPassword(VerifyCodeDto verifyCodeDto) {

		// 이메일 검사
		MemberEntity memberEntity = memberRepository.validateEmail(verifyCodeDto.email());

		// 인증코드 조회
		String verifyCode = memberRepository.getCode(verifyCodeDto.email());

		// 인증코드 검사
		memberDomain.verifyCode(verifyCodeDto.code(), verifyCode);

		// 인증코드 삭제
		memberRepository.deleteCode(verifyCodeDto.email());

	}

	public void resetPassword(PasswordResetRequestDto passwordResetRequestDto) {

		// 이메일 검사
		MemberEntity memberEntity = memberRepository.validateEmail(passwordResetRequestDto.email());

		// 비밀번호 검증
		memberDomain.validateResetPassword(passwordResetRequestDto.password(), passwordResetRequestDto.checkPassword());

		// 비밀번호 재설정
		memberEntity.updateEncodedPassword(encoder.encode(passwordResetRequestDto.password()));

		// 비밀번호 저장
		memberRepository.saveMember(memberEntity);

	}

	public GetMemberDetailResponseDto getMemberDetail(GetMemberDetailDto getMemberDetailDto) {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(getMemberDetailDto.memberSeq());


		return new GetMemberDetailResponseDto(memberEntity.getUserName(), memberEntity.getEmail(), memberEntity.getProfileImage());

	}

	public void modifyProfileImage(ModifyProfileImage modifyProfileImage) throws IOException {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(modifyProfileImage.memberSeq());

		// 프로필 사진 변경
		String uploadImage = s3Util.uploadMultipartFile(modifyProfileImage.multipartFile());
		memberEntity.updateProfileImage(uploadImage);

		// 변경 저장
		memberRepository.saveMember(memberEntity);
	}

	public void logout(LogoutRequestDto logoutRequest) {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(logoutRequest.memberSeq());

		// 파이어 베이스 토큰 삭제
		memberDomain.fireBaseTargetToken(memberEntity);

		// jwt 삭제
		memberRepository.deleteJwt(memberEntity.getEmail());

		// 변경 저장
		memberRepository.saveMember(memberEntity);

	}

	public SearchMemberResponseDto searchMember(SearchMemberDto searchMemberDto) {

		//memberCode 조회
		MemberEntity memberEntity = memberRepository.findByMemberCode(searchMemberDto.memberCode());

		return new SearchMemberResponseDto(memberEntity.getUserName(), memberEntity.getMemberSeq(), memberEntity.getProfileImage());


	}

	public void modifyUserName(ModifyUserNameDto modifyUserNameRequest) {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(modifyUserNameRequest.memberSeq());
		// userName 변경
		memberEntity.updateUserName(modifyUserNameRequest.userName());
		// 변경 저장
		memberRepository.saveMember(memberEntity);

	}
}

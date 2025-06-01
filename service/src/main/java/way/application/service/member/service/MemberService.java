package way.application.service.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.domain.member.MemberDomain;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomMemberRepositoryImpl;
import way.application.infrastructure.jpa.chatRoom.repository.ChatRoomRepositoryImpl;
import way.application.infrastructure.jpa.comment.repository.CommentRepository;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.friend.respository.FriendRepository;
import way.application.infrastructure.jpa.friendRequest.respository.FriendRequestRepository;
import way.application.infrastructure.jpa.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.jpa.location.repository.LocationRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.comment.mapper.CommentMapper;
import way.application.service.member.dto.request.MemberRequestDto.SaveMemberRequestDto;
import way.application.service.member.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import way.application.utils.exception.ConflictException;
import way.application.utils.s3.S3Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static way.application.service.member.dto.request.MemberRequestDto.*;
import static way.application.service.member.dto.response.MemberResponseDto.*;
import static way.application.service.member.util.MemberUtil.*;

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
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final ScheduleRepository scheduleRepository;
	private final FriendRequestRepository friendRequestRepository;
	private final FriendRepository friendRepository;
	private final LocationRepository locationRepository;
	private final FeedRepository feedRepository;
	private final FeedImageRepository feedImageRepository;
	private final HideFeedRepository hideFeedRepository;
	private final BookMarkRepository bookMarkRepository;
	private final ChatRoomMemberRepositoryImpl chatRoomMemberRepository;
	private final ChatRoomRepositoryImpl chatRoomRepository;
	private final CommentRepository commentRepository;
	private final CommentMapper commentMapper;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public void saveMember(SaveMemberRequestDto saveMemberRequestDto) {

		//멤버 유효성 검사
		memberRepository.isDuplicatedEmail(saveMemberRequestDto.email());

		String memberCode = memberDomain.generateMemberCode();

		// Member 저장
		memberRepository.saveMember(
				memberMapper.toMemberEntity(saveMemberRequestDto,encoder.encode(saveMemberRequestDto.password()),memberCode, initialMemberProfileImage)
		);

	}

	public CheckEmailResponseDto checkEmail(CheckEmailRequestDto checkEmailRequestDto) {

		//email 중복 검사
		List<String> type = new ArrayList<>();
		try {
			memberRepository.isDuplicatedEmail(checkEmailRequestDto.email());
		} catch (ConflictException e) {
			MemberEntity memberEntity = memberRepository.findByEmail(checkEmailRequestDto.email());

			if(memberEntity.getEncodedPassword() != null) {
				type.add("normal");
			}
			if(memberEntity.getKakaoPassword() != null) {
				type.add("kakao");
			}
			if(memberEntity.getApplePassword() != null) {
				type.add("apple");
			}

			return new CheckEmailResponseDto(checkEmailRequestDto.email(), type);
		}

		return new CheckEmailResponseDto(checkEmailRequestDto.email(), type);
	}

	public LoginResponseDto login(LoginRequestDto loginRequestDto) {

		// 이메일 검증
		MemberEntity memberEntity = memberRepository.validateEmail(loginRequestDto.email());

		// 일반 로그인 비번 검증
		if(loginRequestDto.loginType().equals("normal")) {
			memberDomain.checkPassword(loginRequestDto.password(), memberEntity.getEncodedPassword());
		}

		// 카카오 로그인 비번 검증
		if(loginRequestDto.loginType().equals("kakao")) {
			memberDomain.checkPassword(loginRequestDto.password(), memberEntity.getKakaoPassword());
		}

		// 애플 로그인 비번 검증
		if(loginRequestDto.loginType().equals("apple")) {
			memberDomain.checkPassword(loginRequestDto.password(), memberEntity.getApplePassword());
		}

		// jwt 생성
		String accessToken = memberDomain.generateAccessToken(memberEntity.getMemberSeq());
		String refreshToken = memberDomain.generateRefreshToken(memberEntity.getMemberSeq());

		// refreshToken 저장
		memberRepository.saveRefreshToken(refreshToken, String.valueOf(memberEntity.getMemberSeq()));

		// fcm 저장
		memberEntity.saveFireBaseTargetToken(loginRequestDto.fcmToken());
		memberRepository.saveMember(memberEntity);

		return new LoginResponseDto(accessToken,refreshToken,memberEntity.getMemberSeq(),memberEntity.getMemberCode(),memberEntity.getProfileImage());
	}

	@Async("mailExecutor")
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

	public String modifyProfileImage(ModifyProfileImage modifyProfileImage) throws IOException {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(modifyProfileImage.memberSeq());

		// 프로필 사진 변경
		String uploadImage = s3Util.uploadMultipartFile(modifyProfileImage.multipartFile());
		memberEntity.updateProfileImage(uploadImage);

		// 변경 저장
		memberRepository.saveMember(memberEntity);

		return uploadImage;
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

	public void saveSnsMember(SaveSnsMemberRequestDto saveSnsMemberRequestDto) {

		//멤버 유효성 검사
//		memberRepository.isDuplicatedEmail(saveSnsMemberRequestDto.email());

		String memberCode = memberDomain.generateMemberCode();

		// Member 저장
		if(saveSnsMemberRequestDto.loginType().equals("kakao")) {
			memberRepository.saveMember(
					memberMapper.toKakaoMemberEntity(saveSnsMemberRequestDto, encoder.encode(saveSnsMemberRequestDto.password()), memberCode)
			);
		}

		if(saveSnsMemberRequestDto.loginType().equals("apple")) {
			memberRepository.saveMember(
					memberMapper.toAppleMemberEntity(saveSnsMemberRequestDto, encoder.encode(saveSnsMemberRequestDto.password()), memberCode)
			);
		}
	}

	public void linkMember(SaveSnsMemberRequestDto saveSnsMemberRequestDto) {

		MemberEntity memberEntity = memberRepository.findByEmail(saveSnsMemberRequestDto.email());

		if(saveSnsMemberRequestDto.loginType().equals("kakao")) {
			memberEntity.updateKakaoPassword(encoder.encode(saveSnsMemberRequestDto.password()));
		}

		if(saveSnsMemberRequestDto.loginType().equals("apple")) {
			memberEntity.updateApplePassword(encoder.encode(saveSnsMemberRequestDto.password()));
		}

		if (saveSnsMemberRequestDto.loginType().equals("normal")) {
			memberEntity.updateEncodedPassword(encoder.encode(saveSnsMemberRequestDto.password()));
		}

		memberRepository.saveMember(memberEntity);
	}

	@Transactional
	public void deleteMember(DeleteMemberDto deleteMemberDtoRequest) {

		// memberSeq 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(deleteMemberDtoRequest.memberSeq());

		// 일반 로그인 시 비번 검사
		if(deleteMemberDtoRequest.loginType().equals("normal")){
			// 비번 검사
			memberDomain.checkPassword(deleteMemberDtoRequest.password(), memberEntity.getEncodedPassword());
		}

		List<ScheduleEntity> schedulesIfCreatedByMember = scheduleMemberRepository.findSchedulesIfCreatedByMember(memberEntity);

		// 친구요청 삭제
		friendRequestRepository.deleteAllByMemberSeq(memberEntity);
		// 친구 삭제
		friendRepository.deleteAllByMemberSeq(memberEntity);
		// 즐겨찾기 위치 삭제
		locationRepository.deleteAllByMemberSeq(memberEntity);
		// 채팅 멤버 삭제
		chatRoomMemberRepository.deleteAllByMemberSeq(memberEntity);
		// 채팅방 삭제
		chatRoomRepository.deleteAllByMemberSeq(schedulesIfCreatedByMember);
		// 북마크 삭제
		bookMarkRepository.deleteAllByMemberSeq(memberEntity);
		// 피드 이미지 삭제
		feedImageRepository.deleteAllByMemberSeq(memberEntity);
		// 피드 삭제
		feedRepository.deleteAllByMemberSeq(memberEntity);
		// 숨긴 피드 삭제
		hideFeedRepository.deleteAllByMemberSeq(memberEntity);
		// 일정 멤버 삭제
		scheduleMemberRepository.deleteAllByMemberSeq(memberEntity, schedulesIfCreatedByMember);
		// 일정 삭제
		scheduleRepository.deleteAllByMemberSeq(memberEntity, schedulesIfCreatedByMember);
		//회원삭제
		memberRepository.deleteByMemberSeq(memberEntity);

		// comment 저장
		commentRepository.saveComment(
				commentMapper.toCommentEntity(deleteMemberDtoRequest)
		);
	}

	public TokenReissueResponseDto reissueToken(TokenReissueRequestDto tokenReissueRequest) {

		Long memberSeq = memberDomain.validateToken(tokenReissueRequest.refreshToken());

		memberDomain.validateRefreshToken(memberSeq, tokenReissueRequest.refreshToken());

		redisTemplate.delete(String.valueOf(memberSeq));

		String accessToken = memberDomain.generateAccessToken(memberSeq);
		String refreshToken = memberDomain.generateRefreshToken(memberSeq);

		memberRepository.saveRefreshToken(refreshToken, String.valueOf(memberSeq));

		return new TokenReissueResponseDto(accessToken, refreshToken, memberSeq);
	}

	public void validateEmail(String email) {

		memberRepository.isDuplicatedEmail(email);
	}
}

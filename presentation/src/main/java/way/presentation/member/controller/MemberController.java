package way.presentation.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import way.application.service.member.service.MemberService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.member.validates.*;
import way.presentation.member.vo.req.MemberRequestVo;
import way.presentation.base.BaseResponse;


import static way.application.service.member.dto.request.MemberRequestDto.*;
import static way.application.service.member.dto.response.MemberResponseDto.*;
import static way.presentation.member.vo.req.MemberRequestVo.*;
import static way.presentation.member.vo.res.MemberResponseVo.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "회원", description = "담당자 (송인준)")
public class MemberController {

    private final SaveMemberValidator saveMemberValidator;
    private final LoginValidator loginValidator;
    private final MemberService memberService;
    private final SendEmailRequestValidator sendEmailRequestValidator;
    private final VerifyCodeValidator verifyCodeValidator;
    private final ResetPasswordValidator resetPasswordValidator;
    private final LogoutValidator logoutValidator;
    private final ModifyUserNameValidator modifyUserNameValidator;
    private final SaveSnsMemberValidator saveSnsMemberValidator;
    private final DeleteMemberValidator deleteMemberValidator;
    private final TokenReissueValidator tokenReissueValidator;

    @PostMapping(name = "회원가입")
    @Operation(summary = "join Member API", description = "회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EDC002",
                    description = "409 EMAIL_DUPLICATION_CONFLICT_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> saveMember(@Valid @RequestBody SaveMemberRequest request) {

        // DTO 유효성 검사
        saveMemberValidator.validate(request);

        // VO -> DTO 변환
        SaveMemberRequestDto saveMemberRequestDto = request.toSaveMemberRequestDto();
        memberService.saveMember(saveMemberRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/sns", name = "Sns 회원가입")
    @Operation(summary = "join Sns Member API", description = "Sns 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EDC002",
                    description = "409 EMAIL_DUPLICATION_CONFLICT_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> saveSnsMember(@Valid @RequestBody SaveSnsMemberRequest request) {

        // DTO 유효성 검사
        saveSnsMemberValidator.validate(request);

        // VO -> DTO 변환
        SaveSnsMemberRequestDto saveSnsMemberRequestDto = request.toSaveSnsMemberRequestDto();
        memberService.saveSnsMember(saveSnsMemberRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/link", name = "연동 하기")
    @Operation(summary = "link Member API", description = "회원 연동 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EDC002",
                    description = "409 EMAIL_DUPLICATION_CONFLICT_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> linkMember(@Valid @RequestBody SaveSnsMemberRequest request) {

        // DTO 유효성 검사
        saveSnsMemberValidator.validate(request);

        // VO -> DTO 변환
        SaveSnsMemberRequestDto saveSnsMemberRequestDto = request.toSaveSnsMemberRequestDto();
        memberService.linkMember(saveSnsMemberRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @GetMapping(value = "/checkEmail", name = "이메일 중복 체크")
    @Operation(summary = "Check Email API", description = "이메일 중복 체크 API")
    @Parameters({
            @Parameter(
                    name = "email",
                    description = "email",
                    example = "dlswns@whereareyou.com")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EDC002",
                    description = "409 EMAIL_DUPLICATION_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<CheckEmailResponse>> checkEmail(@Valid @RequestParam("email") String email) {

        // Param -> VO
        CheckEmailRequest request = new CheckEmailRequest(email);

        // VO -> DTO
        CheckEmailResponseDto checkEmailResponseDto = memberService.checkEmail(request.toCheckEmailRequestDto());

        // DTO -> VO
        CheckEmailResponse checkEmailResponse = new CheckEmailResponse(checkEmailResponseDto.email(),checkEmailResponseDto.loginTypeList());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(),checkEmailResponse));
    }

    @PostMapping(value = "/login", name = "로그인")
    @Operation(summary = "Login API", description = "로그인 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EB009",
                    description = "400 EMAIL_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "PB005",
                    description = "400 PASSWORD_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequest request) {

        // DTO 유효성 검사
        loginValidator.validate(request);

        // VO -> DTO 변환
        LoginRequestDto loginRequestDto = request.toLoginRequestDto();
        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), loginResponseDto));
    }

    @PostMapping(value = "/email/send", name = "메일 전송")
    @Operation(summary = "Mail Send API", description = "인증 메일 전송 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> sendMail(@Valid @RequestBody MailSendRequest request) {

        // DTO 유효성 검사
        sendEmailRequestValidator.validate(request);

        // VO -> DTO 변환
        MailSendRequestDto mailSendRequestDto = request.toMailSendRequestDto();
        memberService.send(mailSendRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/email/verify", name = "인증코드 검증 ")
    @Operation(summary = "Code Verify API", description = "인증코드 검증 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "CB011",
                    description = "400 CODE_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EDC002",
                    description = "409 EMAIL_DUPLICATION_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {

        // DTO 유효성 검사
        verifyCodeValidator.validate(request);

        // VO -> DTO 변환
        VerifyCodeDto verifyCodeDto = request.toVerifyCodeDto();
        memberService.verify(verifyCodeDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/email/verifyPassword", name = "비밀번호 재설정 인증코드 검증 ")
    @Operation(summary = "Password Code Verify API", description = "비밀번호 재설정 인증코드 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EB009",
                    description = "400 EMAIL_BAD_REQUEST_EXCEPTION\"",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "CB011",
                    description = "400 CODE_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> verifyPasswordCode(@Valid @RequestBody VerifyCodeRequest request) {

        // DTO 유효성 검사
        verifyCodeValidator.validate(request);

        // VO -> DTO 변환
        VerifyCodeDto verifyCodeDto = request.toVerifyCodeDto();
        memberService.verifyPassword(verifyCodeDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/resetPassword", name = "비밀번호 재설정")
    @Operation(summary = "Password Code Verify API", description = "비밀번호 재설정 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "PB005",
                    description = "400 PASSWORD_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "PMB013",
                    description = "400 PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EB009",
                    description = "400 EMAIL_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
    })
    public ResponseEntity<BaseResponse<String>> resetPassword(@Valid @RequestBody PasswordResetRequest request) {


        // DTO 유효성 검사
        resetPasswordValidator.validate(request);

        // VO -> DTO 변환
        PasswordResetRequestDto passwordResetRequestDto = request.toPasswordResetRequestDto();
        memberService.resetPassword(passwordResetRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }


    @GetMapping(value = "/details", name = "회원 상세 정보")
    @Operation(summary = "Get Member Details API", description = "회원 상세 정보(By MemberSeq) API")
    @Parameters({
            @Parameter(
                    name = "MemberSeq",
                    description = "Member Sequence",
                    example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<GetMemberDetailResponse>> getMemberDetail(@Valid @RequestParam("memberSeq") Long memberSeq) {

        // Param -> VO
        GetMemberDetailRequest request = new GetMemberDetailRequest(memberSeq);

        // VO -> DTO
        GetMemberDetailResponseDto getMemberDetailResponseDto = memberService.getMemberDetail(request.toGetMemberDetailDto());

        // DTO -> VO
        GetMemberDetailResponse getMemberDetailResponse = new GetMemberDetailResponse(getMemberDetailResponseDto.userName(), getMemberDetailResponseDto.email(), getMemberDetailResponseDto.profileImage());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(),getMemberDetailResponse));
    }

    @GetMapping(value = "/search", name = "회원 검색")
    @Operation(summary = "Get Member Details API", description = "회원 검색(By MemberCode) API")
    @Parameters({
            @Parameter(
                    name = "memberCode",
                    description = "Member Code",
                    example = "1A2B3C")
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MCB021",
                    description = "400 MEMBER_CODE_BAD_REQUEST_EXCEPTION ",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<SearchMemberResponse>> searchMember(@Valid @RequestParam("memberCode") String memberCode) {

        // Param -> VO
        SearchMember request = new SearchMember(memberCode);

        // VO -> DTO
        SearchMemberResponseDto searchMemberResponseDto = memberService.searchMember(request.toSearchMemberDto());

        // DTO -> VO
        SearchMemberResponse searchMemberResponse = new SearchMemberResponse(searchMemberResponseDto.userName(), searchMemberResponseDto.memberSeq(), searchMemberResponseDto.profileImage());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(),searchMemberResponse));
    }

    @PutMapping(value = "/profile-image", name = "회원 사진 변경")
    @Operation(summary = "Modify profileImage API", description = "프로필 사진 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "UID001",
                    description = "409 USER_ID_DUPLICATION_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> ModifyProfileImage(@RequestPart(value = "memberSeq") Long memberSeq,
                                                       @RequestPart(value = "images", required = false) MultipartFile multipartFile
                                                       ) throws Exception {

        // Part -> VO
        MemberRequestVo.ModifyProfileImage request = new MemberRequestVo.ModifyProfileImage(memberSeq, multipartFile);
        // VO -> DTO
        memberService.modifyProfileImage(request.toModifyProfileImage());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/logout", name = "로그아웃")
    @Operation(summary = "Logout API", description = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> logout(@Valid @RequestBody LogoutRequest request) {

        // DTO 유효성 검사
        logoutValidator.validate(request);

        // VO -> DTO 변환
        LogoutRequestDto logoutRequest = request.toLogoutRequest();
        memberService.logout(logoutRequest);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PutMapping(value = "/user-name", name = "회원 이름 변경")
    @Operation(summary = "Modify UserName API", description = "이름 변경 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> ModifyUserName(@RequestBody ModifyUserNameRequest request) {

        // DTO 유효성 검사
        modifyUserNameValidator.validate(request);

        // VO -> DTO 변환
        ModifyUserNameDto modifyUserNameRequest = request.toModifyUserNameRequest();
        memberService.modifyUserName(modifyUserNameRequest);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @DeleteMapping(value = "/member", name = "회원 탈퇴")
    @Operation(summary = "Delete Member API", description = "회원 탈퇴 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> DeleteMember(@RequestBody DeleteMemberRequest request) {

        // DTO 유효성 검사
        deleteMemberValidator.validate(request);

        // VO -> DTO 변환
        DeleteMemberDto deleteMemberDtoRequest = request.toDeleteMemberDtoRequest();
        memberService.deleteMember(deleteMemberDtoRequest);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/tokenReissue", name = "토큰 재발행")
    @Operation(summary = "TokenReissue API", description = "토큰 재발행 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청에 성공하였습니다.",
                    useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "B001",
                    description = "400 Invalid DTO Parameter errors",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "S500",
                    description = "500 SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<TokenReissueResponse>> reissue(@Valid @RequestBody TokenReissueRequest request) {

        // DTO 유효성 검사
        tokenReissueValidator.validate(request);

        // VO -> DTO 변환
        TokenReissueRequestDto tokenReissueRequest = request.toTokenReissueRequest();
        TokenReissueResponseDto tokenReissueResponseDto = memberService.reissueToken(tokenReissueRequest);

        TokenReissueResponse tokenReissueResponse = new TokenReissueResponse(tokenReissueResponseDto.accessToken(),tokenReissueResponseDto.refreshToken(),tokenReissueResponseDto.memberSeq());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), tokenReissueResponse));
    }
}

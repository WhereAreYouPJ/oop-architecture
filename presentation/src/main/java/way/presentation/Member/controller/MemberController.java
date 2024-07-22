package way.presentation.Member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import way.application.service.member.service.MemberService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.Member.validates.*;
import way.presentation.Member.vo.req.MemberRequestVo;
import way.presentation.base.BaseResponse;


import static way.application.service.member.dto.request.MemberRequestDto.*;
import static way.application.service.member.dto.response.MemberResponseDto.*;
import static way.presentation.Member.vo.req.MemberRequestVo.*;
import static way.presentation.Member.vo.res.MemberResponseVo.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final SaveMemberValidator saveMemberValidator;
    private final LoginValidator loginValidator;
    private final MemberService memberService;
    private final SendEmailRequestValidator sendEmailRequestValidator;
    private final VerifyCodeValidator verifyCodeValidator;
    private final ResetPasswordValidator resetPasswordValidator;

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
                    responseCode = "UIDC001",
                    description = "409 USER_ID_DUPLICATION_CONFLICT_EXCEPTION",
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

    @GetMapping(value = "/checkId", name = "아이디 중복 체크")
    @Operation(summary = "Check Id API", description = "아이디 중복 체크 API")
    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "userId",
                    example = "dlswns97")
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
                    responseCode = "UIDC001",
                    description = "409 USER_ID_DUPLICATION_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<CheckIdResponse>> checkId(@Valid @RequestParam("userId") String userId) {

        // Param -> VO
        MemberRequestVo.CheckIdRequest request = new MemberRequestVo.CheckIdRequest(userId);

        // VO -> DTO
        CheckIdResponseDto checkIdResponseDto = memberService.checkId(request.toCheckIdRequestDto());

        // DTO -> VO
        CheckIdResponse checkIdResponse = new CheckIdResponse(checkIdResponseDto.userId());

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(),checkIdResponse));
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
        MemberRequestVo.CheckEmailRequest request = new MemberRequestVo.CheckEmailRequest(email);

        // VO -> DTO
        CheckEmailResponseDto checkEmailResponseDto = memberService.checkEmail(request.toCheckEmailRequestDto());

        // DTO -> VO
        CheckEmailResponse checkEmailResponse = new CheckEmailResponse(checkEmailResponseDto.email());

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
    public ResponseEntity<BaseResponse<LoginResponseDto>> login(@Valid @RequestBody MemberRequestVo.LoginRequest request) {

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
    public ResponseEntity<BaseResponse<String>> sendMail(@Valid @RequestBody MemberRequestVo.MailSendRequest request) {

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
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> verifyCode(@Valid @RequestBody MemberRequestVo.VerifyCodeRequest request) {

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
    public ResponseEntity<BaseResponse<String>> verifyPasswordCode(@Valid @RequestBody MemberRequestVo.VerifyCodeRequest request) {

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
    public ResponseEntity<BaseResponse<String>> resetPassword(@Valid @RequestBody MemberRequestVo.PasswordResetRequest request) {


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
}

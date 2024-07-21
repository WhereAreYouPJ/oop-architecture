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
import way.presentation.Member.validates.LoginValidator;
import way.presentation.Member.validates.SaveMemberValidator;
import way.presentation.Member.validates.SendEmailRequestValidator;
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
    @Operation(summary = "Check Email API", description = "아이디ㅠ 중복 체크 API")
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
                    responseCode = "EB005",
                    description = "400 EMAIL_BAD_REQUEST_EXCEPTION",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "PB006",
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

    @PostMapping(value = "/mail/send", name = "메일 전송")
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
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "EB009",
                    description = "400 Invalid Email errors",
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
}
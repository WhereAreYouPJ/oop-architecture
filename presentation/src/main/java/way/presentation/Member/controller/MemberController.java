package way.presentation.Member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import way.application.service.member.dto.request.MemberRequestDto;
import way.application.service.member.service.MemberService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.Member.validates.SaveMemberValidator;
import way.presentation.base.BaseResponse;

import static way.application.service.member.dto.request.MemberRequestDto.*;
import static way.presentation.Member.vo.req.MemberRequestVo.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final SaveMemberValidator saveMemberValidator;
    private final MemberService memberService;

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
}

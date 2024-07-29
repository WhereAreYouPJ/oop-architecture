package way.presentation.friendRequest.controller;

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
import way.application.service.friendRequest.dto.request.FriendRequestDto;
import way.application.service.friendRequest.service.FriendRequestService;
import way.application.service.member.dto.request.MemberRequestDto;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.friendRequest.validates.SaveFriendRequestValidator;
import way.presentation.friendRequest.vo.req.FriendRequestVo;

import java.io.IOException;

@RestController
@RequestMapping("/friendRequest")
@RequiredArgsConstructor
public class FriendRequestController {

    private final SaveFriendRequestValidator saveFriendRequestValidator;
    private final FriendRequestService friendRequestService;


    @PostMapping(name = "친구 요청")
    @Operation(summary = "friend Request API", description = "친구 요청 API")
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
                    responseCode = "SFRB015",
                    description = "400 SELF_FRIEND_REQUEST_BAD_REQUEST_EXCEPTION / 본인 한테 친구 요청 보낼 수 없음",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "ASB017",
                    description = "400 ALREADY_SENT_BAD_REQUEST_EXCEPTION / 이미 친구 요청 전송됨",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "ASBFB018",
                    description = "400 ALREADY_SENT_BY_FRIEND_BAD_REQUEST_EXCEPTION / 친구가 친구 요청을 이미 보냄",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "SEB022",
                    description = "400 SENDER_SEQ_BAD_REQUEST_EXCEPTION / 신청 보낸 아이디 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "REB023",
                    description = "400 RECEIVER_SEQ_BAD_REQUEST_EXCEPTION / 신청 받은 아이디 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> friendRequest(@Valid @RequestBody FriendRequestVo.SaveFriendRequestRequest request) throws IOException {

        // DTO 유효성 검사
        saveFriendRequestValidator.validate(request);

        // VO -> DTO 변환
        FriendRequestDto.SaveFriendRequestDto saveFriendRequestDto = request.toSaveFriendRequestDto();

        friendRequestService.saveFriendRequest(saveFriendRequestDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}
package way.presentation.friendRequest.controller;

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
import way.application.service.friendRequest.dto.request.FriendRequestDto;
import way.application.service.friendRequest.service.FriendRequestService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.friendRequest.validates.AcceptValidator;
import way.presentation.friendRequest.validates.RefuseValidator;
import way.presentation.friendRequest.validates.SaveFriendRequestValidator;
import way.presentation.friendRequest.vo.req.FriendRequestVo;

import java.io.IOException;
import java.util.List;

import static way.application.service.friendRequest.dto.response.FriendRequestResponseDto.*;
import static way.presentation.friendRequest.vo.res.FriendRequestResponseVo.*;

@RestController
@RequestMapping("/friend-request")
@RequiredArgsConstructor
@Tag(name = "친구 요청", description = "담당자 (송인준)")
public class FriendRequestController {

    private final SaveFriendRequestValidator saveFriendRequestValidator;
    private final FriendRequestService friendRequestService;
    private final AcceptValidator acceptValidator;
    private final RefuseValidator refuseValidator;


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

    @GetMapping(name = "친구 요청 리스트 조회")
    @Operation(summary = "friend Request List API", description = "친구 요청 리스트 조회 API")
    @Parameters({
            @Parameter(
                    name = "memberSeq",
                    description = "memberSeq",
                    example = "1")
    })
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
                    responseCode = "MSB002",
                    description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<List<GetFriendRequestListResponse>>> getFriendRequestList(@Valid @RequestParam("memberSeq") Long memberSeq) {

        // Param -> VO
        FriendRequestVo.GetFriendRequestList request = new FriendRequestVo.GetFriendRequestList(memberSeq);

        // VO -> DTO
        List<FriendRequestList> friendRequestList
                = friendRequestService.getFriendRequestList(request.toGetFriendRequestList());

        // DTO -> VO
        List<GetFriendRequestListResponse> response = friendRequestList.stream()
                .map(dto -> new GetFriendRequestListResponse(
                        dto.friendRequestSeq(),
                        dto.senderSeq(),
                        dto.createTime(),
                        dto.profileImage(),
                        dto.userName()))
                .toList();


        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PostMapping(value = "/accept",name = "친구 요청 수락")
    @Operation(summary = "friend Request Accept API", description = "친구 요청 수락 API")
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
                    responseCode = "FSB026",
                    description = "400 FRIENDREQUEST_SEQ_BAD_REQUEST_EXCEPTION / FRIENDREQUEST_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "SMB027",
                    description = "400 SENDER_SEQ_MISMATCH_REQUEST_EXCEPTION / 친구 요청 Sender와 불일치 ",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "RMB028",
                    description = "400 RECEIVER_SEQ_MISMATCH_REQUEST_EXCEPTION / 친구 요청 Receiver와 불일치",
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
    public ResponseEntity<BaseResponse<String>> accept(@Valid @RequestBody FriendRequestVo.Accept request) {

        // DTO 유효성 검사
        acceptValidator.validate(request);

        // VO -> DTO 변환
        FriendRequestDto.AcceptDto acceptDto = request.toAccept();

        friendRequestService.accept(acceptDto);


        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @DeleteMapping(value = "/refuse",name = "친구 요청 거절")
    @Operation(summary = "friend Request Refuse API", description = "친구 요청 거절 API")
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
                    responseCode = "FSB026",
                    description = "400 FRIENDREQUEST_SEQ_BAD_REQUEST_EXCEPTION / FRIENDREQUEST_SEQ 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> refuse(@Valid @RequestBody FriendRequestVo.Refuse request) {

        // DTO 유효성 검사
        refuseValidator.validate(request);

        // VO -> DTO 변환
        FriendRequestDto.RefuseDto refuseDto = request.toRefuse();

        friendRequestService.refuse(refuseDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}

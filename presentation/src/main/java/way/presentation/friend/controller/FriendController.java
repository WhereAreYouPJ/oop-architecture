package way.presentation.friend.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import way.application.service.friend.dto.response.FriendResponseDto;
import way.application.service.friend.service.FriendService;
import way.application.service.friendRequest.dto.response.FriendRequestResponseDto;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.friend.vo.req.FriendVo;
import way.presentation.friend.vo.res.FriendResponseVo;
import way.presentation.friendRequest.vo.res.FriendRequestResponseVo;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
@Tag(name = "친구", description = "담당자 (송인준)")
public class FriendController {

    private final FriendService friendService;

    @GetMapping(name = "친구 리스트 조회")
    @Operation(summary = "friend List API", description = "친구 리스트 조회 API")
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
    public ResponseEntity<BaseResponse<List<FriendResponseVo.GetFriendListResponse>>> getFriendList(@Valid @RequestParam("memberSeq") Long memberSeq) {

        // Param -> VO
        FriendVo.GetFriendList request = new FriendVo.GetFriendList(memberSeq);

        // VO -> DTO
        List<FriendResponseDto.GetFriendList> friendList
                = friendService.getFriendList(request.toGetFriendList());


        List<FriendResponseVo.GetFriendListResponse> response = friendList.stream()
                .map(dto -> new FriendResponseVo.GetFriendListResponse(
                        dto.memberSeq(),
                        dto.userName(),
                        dto.profileImage()))
                .toList();


        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

}

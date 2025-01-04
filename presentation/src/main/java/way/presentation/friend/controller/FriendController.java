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
import org.springframework.web.bind.annotation.*;
import way.application.service.friend.dto.response.FriendResponseDto;
import way.application.service.friend.service.FriendService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.friend.validates.AddFavoritesValidator;
import way.presentation.friend.validates.DeleteFriendValidator;
import way.presentation.friend.validates.RemoveFavoritesValidator;
import way.presentation.friend.vo.req.FriendVo;

import java.util.List;

import static way.application.service.friend.dto.request.FriendDto.*;
import static way.presentation.friend.vo.req.FriendVo.*;
import static way.presentation.friend.vo.res.FriendResponseVo.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
@Tag(name = "친구", description = "담당자 (송인준)")
public class FriendController {

    private final FriendService friendService;
    private final DeleteFriendValidator deleteFriendValidator;
    private final AddFavoritesValidator addFavoritesValidator;
    private final RemoveFavoritesValidator removeFavoritesValidator;

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
    public ResponseEntity<BaseResponse<List<GetFriendListResponse>>> getFriendList(@Valid @RequestParam("memberSeq") Long memberSeq) {

        // Param -> VO
        GetFriendList request = new GetFriendList(memberSeq);

        // VO -> DTO
        List<FriendResponseDto.GetFriendList> friendList
                = friendService.getFriendList(request.toGetFriendList());


        List<GetFriendListResponse> response = friendList.stream()
                .map(dto -> new GetFriendListResponse(
                        dto.memberSeq(),
                        dto.userName(),
                        dto.profileImage(),
                        dto.favorites()))
                .toList();


        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @DeleteMapping(name = "친구 삭제")
    @Operation(summary = "friend Delete API", description = "친구 삭제 API")
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
    public ResponseEntity<BaseResponse<String>> deleteFriend(@Valid @RequestBody DeleteFriend request) {

        // DTO 유효성 검사
        deleteFriendValidator.validate(request);

        // VO -> DTO 변환
        DeleteFriendDto deleteFriendDto = request.toDeleteFriend();

        friendService.delete(deleteFriendDto);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/favorites", name = "친구 즐찾 추가")
    @Operation(summary = "Modify UserName API", description = "친구 즐찾 추가 API")
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
                    responseCode = "FN002",
                    description = "404 FRIEND_NOT_FOUND_EXCEPTION / 친구 아님 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> AddFavorites(@RequestBody FriendVo.AddFavorites request) {

        // DTO 유효성 검사
        addFavoritesValidator.validate(request);

        // VO -> DTO 변환
        AddFavoritesDto addFavoritesRequest = request.toAddFavorites();
        friendService.addFavorites(addFavoritesRequest);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @DeleteMapping(value = "/favorites", name = "친구 즐찾 제거")
    @Operation(summary = "Modify UserName API", description = "친구 즐찾 제거 API")
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
                    responseCode = "FN002",
                    description = "404 FRIEND_NOT_FOUND_EXCEPTION / 친구 아님 오류",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<BaseResponse<String>> RemoveFavorites(@RequestBody FriendVo.RemoveFavorites request) {

        // DTO 유효성 검사
        removeFavoritesValidator.validate(request);

        // VO -> DTO 변환
        RemoveFavoritesDto removeFavorites = request.toRemoveFavorites();
        friendService.removeFavorites(removeFavorites);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

}

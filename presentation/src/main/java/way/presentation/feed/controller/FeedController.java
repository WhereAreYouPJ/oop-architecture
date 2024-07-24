package way.presentation.feed.controller;

import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.presentation.feed.vo.req.FeedRequestVo.*;
import static way.presentation.feed.vo.res.FeedResponseVo.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.feed.service.FeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.feed.validates.ModifyFeedValidator;
import way.presentation.feed.validates.SaveFeedValidator;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {
	private final SaveFeedValidator saveFeedValidator;
	private final ModifyFeedValidator modifyFeedValidator;

	private final FeedService feedService;

	@PostMapping(name = "피드 생성")
	@Operation(summary = "피드 생성 API", description = "피드 생성 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			useReturnTypeSchema = true),
		@ApiResponse(
			responseCode = "S500",
			description = "500 SERVER_ERROR (나도 몰라 ..)",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "B001",
			description = "400 Invalid DTO Parameter errors / 요청 값 형식 요류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정에 존재하지 않는 Member의 경우 + Schedule에서 일정을 수락하지 않은 경우 조회 불가",
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
	public ResponseEntity<BaseResponse<SaveFeedResponse>> createFeed(
		@Valid
		@ModelAttribute SaveFeedRequest request
	) throws IOException {
		// 유효성 검사
		saveFeedValidator.validate(request);

		// VO -> DTO
		SaveFeedResponseDto saveFeedResponseDto = feedService.saveFeed(request.toSaveFeedRequest());

		// DTO -> VO
		SaveFeedResponse response = new SaveFeedResponse(saveFeedResponseDto.feedSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping(name = "피드 수정")
	@Operation(summary = "피드 수정 API", description = "피드 수정 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			useReturnTypeSchema = true),
		@ApiResponse(
			responseCode = "S500",
			description = "500 SERVER_ERROR (나도 몰라 ..)",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "B001",
			description = "400 Invalid DTO Parameter errors / 요청 값 형식 요류",
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
			responseCode = "FSB019",
			description = "400 FEED_SEQ_BAD_REQUEST_EXCEPTION / FEED_SEQ 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "FDCBMB020",
			description = "400 FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / Member가 생성한 Feed가 아닌 경우",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<ModifyFeedResponse>> modifyFeed(
		@Valid
		@ModelAttribute ModifyReedRequest request
	) throws IOException {
		// 유효성 검사
		modifyFeedValidator.validate(request);

		// VO -> DTO
		ModifyFeedResponseDto modifyFeedResponseDto = feedService.modifyFeed(request.toModifyFeedRequestDto());

		// DTO -> VO
		ModifyFeedResponse response = new ModifyFeedResponse(modifyFeedResponseDto.feedSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

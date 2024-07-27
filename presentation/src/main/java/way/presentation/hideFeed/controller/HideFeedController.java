package way.presentation.hideFeed.controller;

import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;
import static way.presentation.hideFeed.vo.req.HideFeedRequestVo.*;
import static way.presentation.hideFeed.vo.res.HideFeedResponseVo.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.hideFeed.service.HideFeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.hideFeed.validates.AddHideFeedValidator;

@RestController
@RequestMapping("/hide-feed")
@RequiredArgsConstructor
@Tag(name = "피드 숨김", description = "담당자 (박종훈)")
public class HideFeedController {
	private final AddHideFeedValidator addHideFeedValidator;

	private final HideFeedService hideFeedService;

	@PostMapping(name = "피드  숨김")
	@Operation(summary = "피드 숨김 API", description = "피드 숨김 API")
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
			responseCode = "HFEC003",
			description = "409 HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION / 이미 존재하는 HIDE_FEED 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse> addHideFeed(
		@Valid
		@RequestBody HideFeedRequest request
	) {
		// 유효성 검사
		addHideFeedValidator.validate(request);

		// VO -> DTO
		AddHideFeedResponseDto addHideFeedResponseDto = hideFeedService.addHideFeed(request.toHideFeedRequestDto());

		// DTO -> VO
		AddHideFeedResponse response = new AddHideFeedResponse(addHideFeedResponseDto.hideFeedSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

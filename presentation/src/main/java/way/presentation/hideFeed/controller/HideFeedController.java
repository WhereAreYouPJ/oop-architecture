package way.presentation.hideFeed.controller;

import static way.presentation.hideFeed.vo.req.HideFeedRequestVo.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import way.application.service.hideFeed.service.HideFeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.hideFeed.validates.AddHideFeedValidator;
import way.presentation.hideFeed.validates.DeleteHideFeedValidator;

@RestController
@RequestMapping("/hide-feed")
@RequiredArgsConstructor
public class HideFeedController {
	private final AddHideFeedValidator addHideFeedValidator;
	private final DeleteHideFeedValidator deleteHideFeedValidator;

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
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정에 존재하지 않는 Member의 경우 + Schedule에서 일정을 수락하지 않은 경우 조회 불가",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> addHideFeed(
		@Valid
		@RequestBody HideFeedRequest request
	) {
		// 유효성 검사
		addHideFeedValidator.validate(request);

		// VO -> DTO
		hideFeedService.addHideFeed(request.toHideFeedRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping(name = "피드  복원")
	@Operation(summary = "피드 복원 API", description = "피드 복원 API")
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
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정에 존재하지 않는 Member의 경우 + Schedule에서 일정을 수락하지 않은 경우 조회 불가",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> deleteHideFeed(
		@Valid
		@RequestBody DeleteHideFeedRequest request
	) {
		// 유효성 검사
		deleteHideFeedValidator.validate(request);

		// VO -> DTO
		hideFeedService.deleteHideFeed(request.toDeleteHideFeedRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

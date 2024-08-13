package way.presentation.feed.controller;

import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.presentation.feed.vo.req.FeedRequestVo.*;
import static way.presentation.feed.vo.res.FeedResponseVo.*;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import way.application.service.feed.service.FeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.feed.validates.GetAllFeedValidator;
import way.presentation.feed.validates.ModifyFeedValidator;
import way.presentation.feed.validates.SaveFeedValidator;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "피드", description = "담당자 (박종훈)")
public class FeedController {
	private final SaveFeedValidator saveFeedValidator;
	private final ModifyFeedValidator modifyFeedValidator;
	private final GetAllFeedValidator getAllFeedValidator;

	private final FeedService feedService;

	@PostMapping(name = "피드 생성")
	@Operation(summary = "피드 생성 API", description = "Request: SaveFeedRequest, Response: SaveFeedResponse")
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
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "FDC004",
			description = "409 FEED_DUPLICATION_CONFLICT_EXCEPTION / 이미 작성한 피드가 존재할 때 발생 오류",
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
	@Operation(summary = "피드 수정 API", description = "Request: ModifyReedRequest Response: ModifyFeedResponse")
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

	@GetMapping(name = "피드 조회")
	@Operation(summary = "피드 조회 API")
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
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	@Parameters({
		@Parameter(
			name = "memberSeq",
			description = "Member Seq",
			example = "1",
			required = true),
		@Parameter(
			name = "page",
			description = "페이지 번호 (기본값: 0)",
			example = "0"),
		@Parameter(
			name = "size",
			description = "페이지당 항목 수 (기본값: 10)",
			example = "10")
	})
	public ResponseEntity<BaseResponse<Page<GetAllFeedResponseDto>>> getAllFeed(
		@Valid
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) throws IOException {
		// 유효성 검사
		getAllFeedValidator.validate(memberSeq);

		Pageable pageable = PageRequest.of(page, size);
		Page<GetAllFeedResponseDto> response = feedService.getAllFeed(memberSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

package way.presentation.hideFeed.controller;

import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;
import static way.presentation.hideFeed.vo.req.HideFeedRequestVo.*;
import static way.presentation.hideFeed.vo.res.HideFeedResponseVo.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import way.application.service.hideFeed.service.HideFeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.hideFeed.validates.AddHideFeedValidator;
import way.presentation.hideFeed.validates.DeleteHideFeedValidator;
import way.presentation.hideFeed.validates.GetHideFeedValidator;

@RestController
@RequestMapping("/hide-feed")
@RequiredArgsConstructor
@Tag(name = "피드 숨김", description = "담당자 (박종훈)")
public class HideFeedController {
	private final AddHideFeedValidator addHideFeedValidator;
	private final DeleteHideFeedValidator deleteHideFeedValidator;
	private final GetHideFeedValidator getHideFeedValidator;

	private final HideFeedService hideFeedService;

	@PostMapping(name = "피드  숨김")
	@Operation(summary = "피드 숨김 API", description = "Request: HideFeedRequest, Response: AddHideFeedResponse")
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
		@ApiResponse(
			responseCode = "FDCBMB020",
			description = "400 FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / Member가 작성하지 않은 Feed 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<AddHideFeedResponse>> addHideFeed(
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

	@DeleteMapping(name = "피드  숨김 복원")
	@Operation(summary = "피드  숨김 복원 API", description = "Request: DeleteHideFeedRequest")
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
			responseCode = "HFEN001",
			description = "400 FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / Feed는 존재하지만 HIDE_FEED에 존재하지 않을 때 오류",
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

	@GetMapping(name = "피드 숨김 조회")
	@Operation(summary = "피드 숨김 조회 API", description = "Response: GetHideFeedResponse")
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
		@Parameter(name = "memberSeq", description = "회원 PK 값", example = "1"),
		@Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
		@Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
	})
	public ResponseEntity<BaseResponse<Page<GetHideFeedResponse>>> getHideFeed(
		@Valid
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		// Validate
		getHideFeedValidator.validate(memberSeq);

		Pageable pageable = PageRequest.of(page, size);
		Page<GetHideFeedResponseDto> getHideFeedResponseDtos = hideFeedService.getHideFeed(memberSeq, pageable);

		// DTO를 VO로 변환
		Page<GetHideFeedResponse> responses = getHideFeedResponseDtos.map(dto -> new GetHideFeedResponse(
			dto.profileImage(),
			dto.startTime(),
			dto.location(),
			dto.title(),
			dto.feedImageUrl(),
			dto.content(),
			dto.bookMark()
		));

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), responses));
	}
}

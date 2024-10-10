package way.presentation.hideFeed.controller;

import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;
import static way.presentation.hideFeed.vo.request.HideFeedRequestVo.*;
import static way.presentation.hideFeed.vo.response.HideFeedResponseVo.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import lombok.RequiredArgsConstructor;
import way.application.service.hideFeed.service.HideFeedService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;

@RestController
@RequestMapping("/hide-feed")
@RequiredArgsConstructor
@Tag(name = "피드 숨김", description = "담당자 (박종훈)")
public class HideFeedController {
	private final HideFeedService hideFeedService;

	@PostMapping(name = "피드  숨김")
	@Operation(summary = "피드 숨김 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FSB019", description = "400 FEED SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "HFEC003", description = "404 이미 존재하는 HIDE FEED 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> addHideFeed(@RequestBody AddHideFeedRequest request) {
		request.validateAddHideFeedRequest();

		hideFeedService.addHideFeed(request.toHideFeedRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping(name = "피드  숨김 복원")
	@Operation(summary = "피드  숨김 복원 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "HFN001", description = "404 HIDE FEED 존재하지 않음", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> deleteHideFeed(@RequestBody DeleteHideFeedRequest request) {
		request.validateDeleteHideFeedRequest();

		hideFeedService.deleteHideFeed(request.toDeleteHideFeedRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(name = "피드 숨김 조회")
	@Operation(summary = "피드 숨김 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "회원 PK 값", example = "1"),
		@Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
		@Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<Page<GetHideFeedResponseDto>>> getHideFeed(
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<GetHideFeedResponseDto> responses = hideFeedService.getHideFeed(memberSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), responses));
	}
}

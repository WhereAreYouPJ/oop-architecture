package way.presentation.feed.controller;

import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.presentation.feed.vo.request.FeedRequestVo.*;
import static way.presentation.feed.vo.response.FeedResponseVo.*;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import way.presentation.feed.mapper.FeedResponseMapper;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Tag(name = "피드", description = "담당자 (박종훈)")
public class FeedController {
	private final FeedResponseMapper feedResponseMapper;
	private final FeedService feedService;

	@PostMapping(name = "피드 생성", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "피드 생성 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FDC004", description = "400 이미 작성한 피드가 존재할 때 발생 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<SaveFeedResponse>> createFeed(
		@Valid
		@RequestPart(value = "feedImageList", required = false) List<MultipartFile> feedImageList,
		@RequestPart SaveFeedRequest request
	) throws IOException {
		request.saveFeedRequestValidate();

		SaveFeedResponseDto responseDto = feedService.saveFeed(request.toSaveFeedRequestDto(), feedImageList);
		SaveFeedResponse response = feedResponseMapper.toSaveFeedResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping(name = "피드 수정", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "피드 수정 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FSB019", description = "400 FEED SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FDCBMB020", description = "400 회원이 생성한 피드가 아닙니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<ModifyFeedResponse>> modifyFeed(
		@Valid
		@RequestPart(value = "feedImageList", required = false) List<MultipartFile> feedImageList,
		@RequestPart ModifyFeedRequest request
	) throws IOException {
		request.modifyFeedRequestValidator();

		ModifyFeedResponseDto responseDto = feedService.modifyFeed(request.toModifyFeedRequestDto(), feedImageList);
		ModifyFeedResponse response = feedResponseMapper.toModifyFeedResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/list", name = "피드 리스트 조회")
	@Operation(summary = "피드 리스트 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "Member Seq", example = "1", required = true),
		@Parameter(name = "page", description = "페이지 번호 (기본값: 0)", example = "0"),
		@Parameter(name = "size", description = "페이지당 항목 수 (기본값: 10)", example = "10")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<Page<GetFeedResponseDto>>> getAllFeed(
		@Valid
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<GetFeedResponseDto> response = feedService.getAllFeed(memberSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/details", name = "피드 상세 조회")
	@Operation(summary = "피드 상세 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "Member Seq", example = "1", required = true),
		@Parameter(name = "feedSeq", description = "Feed Seq", example = "1", required = true)
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FSB019", description = "400 FEED SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<GetFeedResponseDto>> getFeed(
		@Valid
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(value = "scheduleSeq") Long scheduleSeq
	) {
		GetFeedResponseDto response = feedService.getFeed(memberSeq, scheduleSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

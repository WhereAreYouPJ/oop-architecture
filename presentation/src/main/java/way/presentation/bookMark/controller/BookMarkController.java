package way.presentation.bookMark.controller;

import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;
import static way.presentation.bookMark.vo.req.BookMarkRequestVo.*;
import static way.presentation.bookMark.vo.res.BookMarkResponseVo.*;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.bookMark.service.BookMarkService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.bookMark.validates.AddBookMarkValidator;
import way.presentation.bookMark.validates.DeleteBookMarkValidator;

@RestController
@RequestMapping("/book-mark")
@RequiredArgsConstructor
@Tag(name = "책갈피", description = "담당자 (박종훈)")
public class BookMarkController {
	private final AddBookMarkValidator addBookMarkValidator;
	private final DeleteBookMarkValidator deleteBookMarkValidator;

	private final BookMarkService bookMarkService;

	@PostMapping(name = "책갈피 추가")
	@Operation(summary = "책갈피 추가 API", description = "책갈피 추가 API")
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
			responseCode = "BMFDC005",
			description = "409 BOOK_MARK_FEED_DUPLICATION_CONFLICT_EXCEPTION / 이미 존재하는 Book Mark Feed 오류",
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
	public ResponseEntity<BaseResponse<AddBookMarkResponse>> addHideFeed(
		@Valid
		@RequestBody AddBookMarRequest request
	) {
		// 유효성 검사
		addBookMarkValidator.validate(request);

		// VO -> DTO
		AddBookMarkResponseDto addBookMarkResponseDto
			= bookMarkService.addBookMarkFeed(request.toAddBookMarkRequestDto());

		// DTO -> VO
		AddBookMarkResponse response = new AddBookMarkResponse(addBookMarkResponseDto.bookMarkSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(name = "피드 책갈피 복원")
	@Operation(summary = "피드 책갈피 복원 API", description = "피드 책갈피 복원 API")
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
			description = "400 FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / Feed는 존재하지만 BOOK MARK에 존재하지 않을 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> deleteHideFeed(
		@Valid
		@RequestBody DeleteBookMarkRequest request
	) {
		// 유효성 검사
		deleteBookMarkValidator.validate(request);

		// VO -> DTO
		bookMarkService.deleteBookMarkFeed(request.toDeleteBookMarkRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

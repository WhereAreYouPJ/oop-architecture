package way.presentation.bookMark.controller;

import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;
import static way.presentation.bookMark.vo.request.BookMarkRequestVo.*;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.bookMark.service.BookMarkService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;

@RestController
@RequestMapping("/book-mark")
@RequiredArgsConstructor
@Tag(name = "책갈피", description = "담당자 (박종훈)")
public class BookMarkController {
	private final BookMarkService bookMarkService;

	@PostMapping(name = "책갈피 추가")
	@Operation(summary = "책갈피 추가 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FSB019", description = "400 FEED SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BMFDC005", description = "409 이미 존재하는 BOOK MARK FEED 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> addHideFeed(@RequestBody AddBookMarkRequest request) {
		request.addBookMarkRequestValidate();

		bookMarkService.addBookMarkFeed(request.toAddBookMarkRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping(name = "피드 책갈피 복원")
	@Operation(summary = "피드 책갈피 복원 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "BMFN004", description = "404 BOOK MARK한 피드가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> deleteBookMark(@RequestBody DeleteBookMarkRequest request) {
		request.deleteBookMarkRequestValidate();

		bookMarkService.deleteBookMarkFeed(request.toDeleteBookMarkRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(name = "책갈피 조회")
	@Operation(summary = "피드 책갈피 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "회원 PK 값", example = "1"),
		@Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
		@Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<Page<GetBookMarkResponseDto>>> getBookMark(
		@Valid
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<GetBookMarkResponseDto> responses = bookMarkService.getBookMark(memberSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), responses));
	}
}

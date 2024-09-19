package way.presentation.admin.controller;

import static way.application.service.admin.dto.response.AdminResponseDto.*;
import static way.presentation.admin.vo.request.AdminRequestVo.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import way.application.service.admin.service.AdminService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "관리자", description = "담당자 (박종훈)")
public class AdminController {
	private final AdminService adminService;

	@PostMapping("/image")
	public ResponseEntity<BaseResponse<AddHomeImageResponseDto>> addHomeImage(
		@ModelAttribute AddHomeImageRequest request
	) throws IOException {
		AddHomeImageResponseDto response = adminService.addHomeImage(request.toAddHomeImageRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping("/image")
	@Operation(summary = "홈 화면 공지사항 사진", description = "홈 화면 공지사항 사진")
	@Parameters({
		@Parameter(
			name = "memberSeq",
			description = "Member Sequence",
			example = "1")
	})
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
			responseCode = "MSB002",
			description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / MEMBER_SEQ 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<List<GetHomeImageResponseDto>>> getHomeImage(
		@RequestParam(value = "memberSeq") Long memberSeq
	) {
		List<GetHomeImageResponseDto> response = adminService.getHomeImage(memberSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping("/image")
	public ResponseEntity<BaseResponse> deleteHomeImage(
		@RequestBody DeleteHomeImageRequest request
	) {
		adminService.deleteHomeImage(request.toDeleteHomeImageRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

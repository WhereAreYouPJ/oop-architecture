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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import way.application.service.admin.service.AdminService;
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
	public ResponseEntity<BaseResponse<List<GetHomeImageResponseDto>>> getHomeImage() {
		List<GetHomeImageResponseDto> response = adminService.getHomeImage();

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

package way.presentation.location.controller;

import static way.application.service.location.dto.response.LocationResponseDto.*;
import static way.presentation.location.vo.req.LocationRequestVo.*;
import static way.presentation.location.vo.res.LocationResponseVo.*;

import java.io.IOException;

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
import way.application.service.location.service.LocationService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.location.validates.AddLocationValidator;
import way.presentation.location.validates.DeleteLocationValidator;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@Tag(name = "위치 즐겨찾기", description = "담당자 (박종훈)")
public class LocationController {
	private final AddLocationValidator addLocationValidator;
	private final DeleteLocationValidator deleteLocationValidator;

	private final LocationService locationService;

	@PostMapping(name = "위치 즐겨찾기 생성")
	@Operation(summary = "위치 즐겨찾기 생성", description = "Request: AddLocationRequest, Response: AddLocationResponse")
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
			description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / 존재하지 않는 MemberSeq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<AddLocationResponse>> addLocation(
		@Valid
		@RequestBody AddLocationRequest request
	) throws IOException {
		// 유효성 검사
		addLocationValidator.validate(request);

		// VO -> DTO
		AddLocationResponseDto addLocationResponseDto = locationService.addLocation(request.toAddLocationRequestDto());

		// DTO -> VO
		AddLocationResponse response = new AddLocationResponse(addLocationResponseDto.locationSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(name = "위치 즐겨찾기 삭제")
	@Operation(summary = "위치 즐겨찾기 삭제", description = "Request: DeleteLocationRequest, Response: AddLocationResponse")
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
			description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / 존재하지 않는 MemberSeq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "LSB025",
			description = "400 LOCATION_SEQ_BAD_REQUEST_EXCEPTION / 존재하지 않는 locationSeq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse> deleteLocation(
		@Valid
		@RequestBody DeleteLocationRequest request
	) {
		// 유효성 검사
		deleteLocationValidator.validate(request);

		locationService.deleteLocation(request.toDeleteLocationRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

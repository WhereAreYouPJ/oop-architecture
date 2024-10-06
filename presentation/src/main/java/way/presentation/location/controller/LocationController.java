package way.presentation.location.controller;

import static way.application.service.location.dto.response.LocationResponseDto.*;
import static way.presentation.location.vo.req.LocationRequestVo.*;
import static way.presentation.location.vo.res.LocationResponseVo.*;

import java.util.List;

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
import way.application.service.location.service.LocationService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.location.mapper.LocationResponseMapper;
import way.presentation.schedule.vo.response.ScheduleResponseVo;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
@Tag(name = "위치 즐겨찾기", description = "담당자 (박종훈)")
public class LocationController {
	private final LocationResponseMapper locationResponseMapper;
	private final LocationService locationService;

	@PostMapping(name = "위치 즐겨찾기 생성")
	@Operation(summary = "위치 즐겨찾기 생성")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<AddLocationResponse>> addLocation(@RequestBody AddLocationRequest request) {
		request.validateAddLocationRequest();

		AddLocationResponseDto responseDto = locationService.addLocation(request.toAddLocationRequestDto());
		AddLocationResponse response = locationResponseMapper.toAddLocationResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(name = "위치 즐겨찾기 삭제")
	@Operation(summary = "위치 즐겨찾기 삭제", description = "Request: DeleteLocationRequest, Response: AddLocationResponse")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "LSB025", description = "LOCATION SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> deleteLocation(@RequestBody DeleteLocationRequest request) {
		request.validateDeleteLocationRequest();

		locationService.deleteLocation(request.toDeleteLocationRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(name = "위치 즐겨찾기 조회")
	@Operation(summary = "위치 즐겨찾기 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "회원 PK 값", example = "1"),
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<GetLocationResponse>>> getBookMark(
		@RequestParam(value = "memberSeq", required = true) Long memberSeq
	) {
		List<GetLocationResponseDto> responseDto = locationService.getLocation(memberSeq);

		List<GetLocationResponse> response = responseDto.stream()
			.map(locationResponseMapper::toGetLocationResponse)
			.toList();

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

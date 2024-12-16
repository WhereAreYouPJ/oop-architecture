package way.presentation.coordinate.controller;

import static way.application.service.coordinate.dto.response.CoordinateResponseDto.*;
import static way.presentation.coordinate.vo.request.CoordinateRequest.*;
import static way.presentation.coordinate.vo.response.CoordinateResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import way.application.service.coordinate.service.CoordinateService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.coordinate.mapper.CoordinateResponseMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coordinate")
@Tag(name = "사용자 위치 정보", description = "담당자 (박종훈)")
public class CoordinateController {
	private final CoordinateService coordinateService;
	private final CoordinateResponseMapper coordinateResponseMapper;

	@PostMapping()
	@Operation(summary = "사용자 위치 정보 생성 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<String>> createCoordinate(@RequestBody CreateCoordinateRequest request) {
		request.validateCreateCoordinateRequest();

		coordinateService.createCoordinate(request.toCreateCoordinateRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping()
	@Operation(summary = "사용자 위치 정보 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "Member Sequence", example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CN005", description = "404 좌표가 존재하지 않습니다", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "STB029", description = "400 START TIME 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<GetCoordinateResponse>> getCoordinate(
		@RequestParam(value = "memberSeq") Long memberSeq,
		@RequestParam(value = "scheduleSeq") Long scheduleSeq
	) {
		GetCoordinateResponseDto responseDto = coordinateService.getCoordinate(memberSeq, scheduleSeq);
		GetCoordinateResponse response = coordinateResponseMapper.toGetCoordinateResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

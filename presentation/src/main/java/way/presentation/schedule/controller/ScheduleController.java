package way.presentation.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.schedule.dto.request.ModifyScheduleRequestDto;
import way.application.service.schedule.dto.request.SaveScheduleRequestDto;
import way.application.service.schedule.dto.response.ModifyScheduleResponseDto;
import way.application.service.schedule.dto.response.SaveScheduleResponseDto;
import way.application.service.schedule.service.ScheduleService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.schedule.validates.ModifyScheduleValidator;
import way.presentation.schedule.validates.SaveScheduleValidator;
import way.presentation.schedule.vo.req.ModifyScheduleRequest;
import way.presentation.schedule.vo.req.SaveScheduleRequest;
import way.presentation.schedule.vo.res.ModifyScheduleResponse;
import way.presentation.schedule.vo.res.SaveScheduleResponse;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	private final SaveScheduleValidator saveScheduleValidator;
	private final ModifyScheduleValidator modifyScheduleValidator;

	private final ScheduleService scheduleService;

	@PostMapping(name = "일정 생성")
	@Operation(summary = "일정 생성 API", description = "일정 생성 API")
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
			responseCode = "S501",
			description = "500 FIREBASE_CLOUD_MESSAGING_EXCEPTION / FIREBASE 오류(서버 오류 혹은 Token 존재 X)",
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
	public ResponseEntity<BaseResponse<SaveScheduleResponse>> saveSchedule(
		@Valid
		@RequestBody SaveScheduleRequest request
	) {
		// DTO 유효성 검사
		saveScheduleValidator.validate(request);

		// VO -> DTO 변환
		SaveScheduleRequestDto scheduleDto = request.toSaveScheduleRequestDto();
		SaveScheduleResponseDto saveScheduleResponseDto = scheduleService.createSchedule(scheduleDto);

		// DTO -> VO 변환
		SaveScheduleResponse response = new SaveScheduleResponse(saveScheduleResponseDto.scheduleSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping(name = "일정 수정")
	@Operation(summary = "일정 수정 API", description = "일정 수정 API")
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
			responseCode = "S501",
			description = "500 FIREBASE_CLOUD_MESSAGING_EXCEPTION / FIREBASE 오류(서버 오류 혹은 Token 존재 X)",
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
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "SDCBMB008",
			description = "400 SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<ModifyScheduleResponse>> modifySchedule(
		@Valid
		@RequestBody ModifyScheduleRequest request
	) {
		// DTO 유효성 검사
		modifyScheduleValidator.validate(request);

		// VO -> DTO 변환
		ModifyScheduleRequestDto scheduleDto = request.toModifyScheduleRequestDto();
		ModifyScheduleResponseDto modifyScheduleResponseDto = scheduleService.modifySchedule(scheduleDto);

		// DTO -> VO 변환
		ModifyScheduleResponse response = new ModifyScheduleResponse(modifyScheduleResponseDto.scheduleSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

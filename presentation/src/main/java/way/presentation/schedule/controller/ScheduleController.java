package way.presentation.schedule.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.schedule.dto.request.ScheduleRequestDto;
import way.application.service.schedule.dto.response.ScheduleResponseDto;
import way.application.service.schedule.service.ScheduleService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.schedule.validates.DeleteScheduleValidator;
import way.presentation.schedule.validates.ModifyScheduleValidator;
import way.presentation.schedule.validates.SaveScheduleValidator;
import way.presentation.schedule.vo.req.ScheduleRequestVo;
import way.presentation.schedule.vo.res.ScheduleResponseVo;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	private final SaveScheduleValidator saveScheduleValidator;
	private final ModifyScheduleValidator modifyScheduleValidator;
	private final DeleteScheduleValidator deleteScheduleValidator;

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
	public ResponseEntity<BaseResponse<ScheduleResponseVo.SaveScheduleResponse>> saveSchedule(
		@Valid
		@RequestBody ScheduleRequestVo.SaveScheduleRequest request
	) {
		// DTO 유효성 검사
		saveScheduleValidator.validate(request);

		// VO -> DTO 변환
		ScheduleRequestDto.SaveScheduleRequestDto scheduleDto = request.toSaveScheduleRequestDto();
		ScheduleResponseDto.SaveScheduleResponseDto saveScheduleResponseDto = scheduleService.createSchedule(
			scheduleDto);

		// DTO -> VO 변환
		ScheduleResponseVo.SaveScheduleResponse response
			= new ScheduleResponseVo.SaveScheduleResponse(saveScheduleResponseDto.scheduleSeq());

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
	public ResponseEntity<BaseResponse<ScheduleResponseVo.ModifyScheduleResponse>> modifySchedule(
		@Valid
		@RequestBody ScheduleRequestVo.ModifyScheduleRequest request
	) {
		// DTO 유효성 검사
		modifyScheduleValidator.validate(request);

		// VO -> DTO 변환
		ScheduleRequestDto.ModifyScheduleRequestDto scheduleDto = request.toModifyScheduleRequestDto();
		ScheduleResponseDto.ModifyScheduleResponseDto modifyScheduleResponseDto = scheduleService.modifySchedule(
			scheduleDto);

		// DTO -> VO 변환
		ScheduleResponseVo.ModifyScheduleResponse response
			= new ScheduleResponseVo.ModifyScheduleResponse(modifyScheduleResponseDto.scheduleSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(name = "일정 삭제")
	@Operation(summary = "일정 삭제 API", description = "일정 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(
					implementation = BaseResponse.class))),
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
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
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
			responseCode = "SDCBMB008",
			description = "400 SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> deleteSchedule(
		@Valid
		@RequestBody ScheduleRequestVo.DeleteScheduleRequest request
	) {
		// DTO 유효성 검사
		deleteScheduleValidator.validate(request);

		// VO -> DTO
		ScheduleRequestDto.DeleteScheduleRequestDto deleteScheduleRequestDto = request.toDeleteScheduleRequestDto();
		scheduleService.deleteSchedule(deleteScheduleRequestDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(name = "일정 조회")
	@Operation(summary = "일정 상세 조회 API", description = "일정 상세 조회 API")
	@Parameters({
		@Parameter(
			name = "scheduleSeq",
			description = "Schedule Sequence",
			example = "1"),
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
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
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
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정에 존재하지 않는 Member의 경우 + Schedule에서 일정을 수락하지 않은 경우 조회 불가",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<ScheduleResponseVo.GetScheduleResponse>> getSchedule(
		@Valid
		@RequestParam(name = "scheduleSeq") Long scheduleSeq,
		@RequestParam(name = "memberSeq") Long memberSeq) {
		ScheduleResponseDto.GetScheduleResponseDto getScheduleResponseDto = scheduleService.getSchedule(scheduleSeq,
			memberSeq);

		// DTO -> VO 변환
		ScheduleResponseVo.GetScheduleResponse response = new ScheduleResponseVo.GetScheduleResponse(
			getScheduleResponseDto.title(),
			getScheduleResponseDto.startTime(),
			getScheduleResponseDto.endTime(),
			getScheduleResponseDto.location(),
			getScheduleResponseDto.streetName(),
			getScheduleResponseDto.x(),
			getScheduleResponseDto.y(),
			getScheduleResponseDto.color(),
			getScheduleResponseDto.memo(),
			getScheduleResponseDto.userName()
		);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/date", name = "해당 날짜 일정 조회")
	@Operation(summary = "해당 날짜 일정 조회 API", description = "해당 날짜 일정 조회 API")
	@Parameters({
		@Parameter(
			name = "date",
			description = "조회하려는 날짜",
			example = "2024-05-12"),
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
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<ScheduleResponseVo.GetScheduleByDateResponse>>> getScheduleByDate(
		@Valid
		@RequestParam(name = "date") LocalDate date,
		@RequestParam(name = "memberSeq") Long memberSeq) {
		// Param -> VO
		ScheduleRequestVo.GetScheduleByDateRequest request
			= new ScheduleRequestVo.GetScheduleByDateRequest(memberSeq, date);

		// VO -> DTO
		List<ScheduleResponseDto.GetScheduleByDateResponseDto> scheduleByDateResponseDto
			= scheduleService.getScheduleByDate(request.toGetScheduleByDateRequestDto());

		// DTO -> VO
		List<ScheduleResponseVo.GetScheduleByDateResponse> response = scheduleByDateResponseDto.stream()
			.map(dto -> new ScheduleResponseVo.GetScheduleByDateResponse(
				dto.scheduleSeq(),
				dto.title(),
				dto.location(),
				dto.color(),
				dto.startTime(),
				dto.endTime()))
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PostMapping(value = "/accept-schedule", name = "일정 초대 수락")
	@Operation(summary = "일정 초대 수락 API", description = "일정 초대 수락 API")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(
					implementation = BaseResponse.class))),
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
		@ApiResponse(
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정에 존재하지 않는 Member의 경우 + Schedule에서 일정을 수락하지 않은 경우 조회 불가",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> acceptSchedule(
		@Valid
		@RequestBody
		ScheduleRequestVo.AcceptScheduleRequest request
	) {
		// VO -> DTO
		scheduleService.acceptSchedule(request.toAcceptScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(value = "/month-schedule", name = "월별 일정 조회")
	@Operation(summary = "월별 일정 조회 API", description = "월별 일정 조회 API")
	@Parameters({
		@Parameter(
			name = "yearMonth",
			description = "조회하려는 날짜(yyyy-dd)",
			example = "2024-05"),
		@Parameter(
			name = "memberSeq",
			description = "Member Sequence",
			example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			useReturnTypeSchema = true
			// content = @Content(
			// 	mediaType = "application/json",
			// 	schema = @Schema(
			// 		implementation = Schedule.GetScheduleByMonthResponse.class,
			// 		type = "array"))
		),
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
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<ScheduleResponseVo.GetScheduleByMonthResponse>>> getScheduleByMonth(
		@Valid
		@DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
		@RequestParam("memberSeq") Long memberSeq
	) {
		// Param -> VO
		ScheduleRequestVo.GetScheduleByMonthRequest requestVO = new ScheduleRequestVo.GetScheduleByMonthRequest(
			yearMonth,
			memberSeq
		);

		// VO -> DTO
		List<ScheduleResponseDto.GetScheduleByMonthResponseDto> responseDto = scheduleService.getScheduleByMonth(
			requestVO.toGetScheduleByMonthRequestDto()
		);

		// DTO -> VO
		List<ScheduleResponseVo.GetScheduleByMonthResponse> response = responseDto.stream()
			.map(scheduleEntity -> new ScheduleResponseVo.GetScheduleByMonthResponse(
				scheduleEntity.scheduleSeq(),
				scheduleEntity.title(),
				scheduleEntity.startTime(),
				scheduleEntity.endTime(),
				scheduleEntity.location(),
				scheduleEntity.streetName(),
				scheduleEntity.x(),
				scheduleEntity.y(),
				scheduleEntity.color(),
				scheduleEntity.memo()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

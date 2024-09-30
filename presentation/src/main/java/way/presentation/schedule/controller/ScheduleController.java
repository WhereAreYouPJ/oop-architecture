package way.presentation.schedule.controller;

import static way.application.service.schedule.dto.request.ScheduleRequestDto.*;
import static way.application.service.schedule.dto.response.ScheduleResponseDto.*;
import static way.presentation.schedule.vo.request.ScheduleRequestVo.*;
import static way.presentation.schedule.vo.response.ScheduleResponseVo.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.schedule.service.ScheduleService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.schedule.mapper.ScheduleResponseMapper;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Tag(name = "일정", description = "담당자 (박종훈)")
public class ScheduleController {
	private final ScheduleService scheduleService;

	private final ScheduleResponseMapper scheduleResponseMapper;

	@PostMapping(name = "일정 생성")
	@Operation(summary = "일정 생성 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "201 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S501", description = "500 FIREBASE CLOUD MESSAGING 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "FN002", description = "404 해당 친구가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<SaveScheduleResponse>> saveSchedule(
		@Valid
		@RequestBody SaveScheduleRequest request
	) {
		// REQUEST Validate
		request.saveScheduleRequestValidate();

		SaveScheduleResponseDto responseDto = scheduleService.createSchedule(request.toSaveScheduleRequestDto());
		SaveScheduleResponse response = scheduleResponseMapper.toSaveScheduleResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping(name = "일정 수정")
	@Operation(summary = "일정 수정 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "S501", description = "500 FIREBASE CLOUD MESSAGING 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SDCBMB008", description = "400 회원이 생성하지 않은 일정입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "STB029", description = "400 START TIME 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<ModifyScheduleResponse>> modifySchedule(
		@Valid
		@RequestBody ModifyScheduleRequest request
	) {
		// REQUEST VALIDATE
		request.modifyScheduleRequestValidate();

		ModifyScheduleResponseDto responseDto = scheduleService.modifySchedule(request.toModifyScheduleRequestDto());
		ModifyScheduleResponse response = scheduleResponseMapper.toModifyScheduleResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(value = "creator", name = "일정 삭제(일정 생성자인 경우)")
	@Operation(summary = "일정 삭제(일정 생성자인 경우) API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SDCBMB008", description = "400 회원이 생성하지 않은 일정입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRN004", description = "404 CHAT ROOM이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> deleteSchedule(
		@Valid
		@RequestBody DeleteScheduleRequest request
	) {
		// REQUEST VALIDATE
		request.deleteScheduleRequestValidate();

		DeleteScheduleRequestDto deleteScheduleRequestDto = request.toDeleteScheduleRequestDto();
		scheduleService.deleteScheduleByCreator(deleteScheduleRequestDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping(value = "invited", name = "일정 삭제(일정 초대자인 경우)")
	@Operation(summary = "일정 삭제(일정 초대자인 경우) API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRN004", description = "404 CHAT ROOM이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> deleteInvitedScheduleMember(
		@Valid
		@RequestBody DeleteScheduleRequest request
	) {
		request.deleteScheduleRequestValidate();

		scheduleService.deleteScheduleMemberByInvitor(request.toDeleteScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(name = "일정 조회")
	@Operation(summary = "일정 상세 조회 API")
	@Parameters({
		@Parameter(name = "scheduleSeq", description = "Schedule Sequence", example = "1"),
		@Parameter(name = "memberSeq", description = "Member Sequence", example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<GetScheduleResponse>> getSchedule(
		@Valid
		@RequestParam(name = "scheduleSeq") Long scheduleSeq,
		@RequestParam(name = "memberSeq") Long memberSeq
	) {
		GetScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleSeq, memberSeq);
		GetScheduleResponse response = scheduleResponseMapper.toGetScheduleResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/date", name = "해당 날짜 일정 조회")
	@Operation(summary = "해당 날짜 일정 조회 API")
	@Parameters({
		@Parameter(name = "date", description = "조회하려는 날짜", example = "2024-05-12"),
		@Parameter(name = "memberSeq", description = "Member Sequence", example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<GetScheduleByDateResponse>>> getScheduleByDate(
		@Valid
		@RequestParam(name = "date") LocalDate date,
		@RequestParam(name = "memberSeq") Long memberSeq
	) {
		List<GetScheduleByDateResponseDto> responseDto = scheduleService.getScheduleByDate(memberSeq, date);

		List<GetScheduleByDateResponse> response = responseDto.stream()
			.map(scheduleResponseMapper::toGetScheduleByDateResponse).collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PostMapping(value = "/accept", name = "일정 초대 수락")
	@Operation(summary = "일정 초대 수락 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> acceptSchedule(
		@Valid
		@RequestBody AcceptScheduleRequest request
	) {
		request.acceptScheduleRequestValidate();

		scheduleService.acceptSchedule(request.toAcceptScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(value = "/month", name = "월별 일정 조회")
	@Operation(summary = "월별 일정 조회 API")
	@Parameters({
		@Parameter(name = "yearMonth", description = "조회하려는 날짜(yyyy-dd)", example = "2024-05"),
		@Parameter(name = "memberSeq", description = "Member Sequence", example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<GetScheduleByMonthResponse>>> getScheduleByMonth(
		@Valid
		@DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
		@RequestParam("memberSeq") Long memberSeq
	) {
		List<GetScheduleByMonthResponseDto> responseDto = scheduleService.getScheduleByMonth(yearMonth, memberSeq);

		List<GetScheduleByMonthResponse> response = responseDto.stream()
			.map(scheduleResponseMapper::toGetScheduleByMonthResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/dday", name = "일정 D-DAY 조회")
	@Operation(summary = "일정 D-DAY 조회 API")
	@Parameters({
		@Parameter(name = "memberSeq", description = "Member Sequence", example = "1")
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<List<GetDdayScheduleResponse>>> getDdaySchedule(
		@Valid
		@RequestParam("memberSeq") Long memberSeq
	) {
		List<GetDdayScheduleResponseDto> responseDto = scheduleService.getDdaySchedule(memberSeq);

		List<GetDdayScheduleResponse> response = responseDto.stream()
			.map(scheduleResponseMapper::toGetDdayScheduleResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/list", name = "일정 List 조회")
	@Operation(summary = "일정 List 조회 API")
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
	public ResponseEntity<BaseResponse<Page<GetScheduleListResponse>>> getScheduleList(
		@Valid
		@RequestParam("memberSeq") Long memberSeq,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {

		Pageable pageable = PageRequest.of(page, size);
		Page<GetScheduleListResponseDto> responseDtoPage = scheduleService.getScheduleList(memberSeq, pageable);

		Page<GetScheduleListResponse> response = responseDtoPage.map(scheduleResponseMapper::toGetScheduleListResponse);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(value = "/refuse", name = "일정 거절")
	@Operation(summary = "일정 거절 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MCSB033", description = "400 회원이 생성한 일정입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MAASB034", description = "400 회원이 이미 일정을 수락했습니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<String>> refuseSchedule(
		@Valid
		@RequestBody RefuseScheduleRequest request
	) {
		// REQUEST VALIDATE
		request.refuseScheduleRequestValidate();

		scheduleService.refuseSchedule(request.toRefuseScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

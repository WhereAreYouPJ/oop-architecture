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
		@ApiResponse(
			responseCode = "201",
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
			responseCode = "FN002",
			description = "404 FRIEND_NOT_FOUND_EXCEPTION / 친구 목록에 없을 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<SaveScheduleResponse>> saveSchedule(
		@Valid
		@RequestBody SaveScheduleRequest request
	) {
		// REQUEST Validate
		request.saveScheduleRequestValidate();

		SaveScheduleResponseDto responseDto = scheduleService.createSchedule(request.toSaveScheduleRequestDto());
		SaveScheduleResponse response = scheduleResponseMapper.toSaveScheduleResponse(responseDto);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.CREATED.value(), response));
	}

	@PutMapping(name = "일정 수정")
	@Operation(summary = "일정 수정 API")
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
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "STB029",
			description = "400 START_TIME_BAD_REQUEST_EXCEPTION / 수정 시간 (시작시간 기준 전후 1시간) 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
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
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "CRN004",
			description = "404 CHAT_ROOM_NOT_FOUND_EXCEPTION / Chat Room 이 생성되어 있지 않을 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> deleteSchedule(
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
	@Operation(summary = "일정 삭제(일정 초대자인 경우) API", description = "Request: DeleteScheduleRequest")
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
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / Schedule에 Member가 존재하지 않는 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "CRN004",
			description = "404 CHAT_ROOM_NOT_FOUND_EXCEPTION / Chat Room 이 생성되어 있지 않을 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> deleteInvitedScheduleMember(
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
	@Operation(summary = "해당 날짜 일정 조회 API", description = "Response: GetScheduleByDateResponse")
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
	@Operation(summary = "일정 초대 수락 API", description = "Request: AcceptScheduleRequest")
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
		@RequestBody AcceptScheduleRequest request
	) {
		request.acceptScheduleRequestValidate();

		scheduleService.acceptSchedule(request.toAcceptScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@GetMapping(value = "/month", name = "월별 일정 조회")
	@Operation(summary = "월별 일정 조회 API", description = "Response: GetScheduleByMonthResponse")
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
	public ResponseEntity<BaseResponse<List<GetScheduleByMonthResponse>>> getScheduleByMonth(
		@Valid
		@DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth,
		@RequestParam("memberSeq") Long memberSeq
	) {
		// Param -> VO
		GetScheduleByMonthRequest requestVO = new GetScheduleByMonthRequest(yearMonth, memberSeq);

		// VO -> DTO
		List<GetScheduleByMonthResponseDto> responseDto = scheduleService.getScheduleByMonth(
			requestVO.toGetScheduleByMonthRequestDto()
		);

		// DTO -> VO
		List<GetScheduleByMonthResponse> response = responseDto.stream()
			.map(scheduleEntity -> new GetScheduleByMonthResponse(
				scheduleEntity.scheduleSeq(),
				scheduleEntity.title(),
				scheduleEntity.startTime(),
				scheduleEntity.endTime(),
				scheduleEntity.location(),
				scheduleEntity.streetName(),
				scheduleEntity.x(),
				scheduleEntity.y(),
				scheduleEntity.color(),
				scheduleEntity.memo(),
				scheduleEntity.allDay()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/dday", name = "일정 D-DAY 조회")
	@Operation(summary = "일정 D-DAY 조회 API", description = "일정 D-DAY 조회 API")
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
	@Parameters({
		@Parameter(
			name = "memberSeq",
			description = "Member Sequence",
			example = "1")
	})
	public ResponseEntity<BaseResponse<List<GetDdayScheduleResponse>>> getDdaySchedule(
		@Valid
		@RequestParam("memberSeq") Long memberSeq) {

		// Param -> VO
		GetDdaySchedule requestVo = new GetDdaySchedule(memberSeq);

		// VO -> DTO

		List<GetDdayScheduleResponseDto> responseDto = scheduleService.getDdaySchedule(
			requestVo.toGetDdayScheduleDto());

		// DTO -> VO
		List<GetDdayScheduleResponse> response = responseDto.stream()
			.map(scheduleEntity -> new GetDdayScheduleResponse(
				scheduleEntity.scheduleSeq(),
				scheduleEntity.title(),
				scheduleEntity.dDay()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping(value = "/list", name = "일정 List 조회")
	@Operation(summary = "일정 List 조회 API", description = "일정 List 조회 API")
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
	@Parameters({
		@Parameter(name = "memberSeq", description = "회원 PK 값", example = "1"),
		@Parameter(name = "page", description = "페이지 처리 페이지 수", example = "0"),
		@Parameter(name = "size", description = "페이지 당 응답 받을 데이터 개수", example = "10"),
	})
	public ResponseEntity<BaseResponse<Page<GetScheduleListDto>>> getScheduleList(
		@Valid
		@RequestParam("memberSeq") Long memberSeq,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {

		Pageable pageable = PageRequest.of(page, size);
		Page<GetScheduleListDto> response = scheduleService.getScheduleList(memberSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping(value = "/refuse", name = "일정 거절")
	@Operation(summary = "일정 거절 API", description = "일정 거절 API")
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
		@ApiResponse(
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / SCHEDULE_ID 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MCSB033",
			description = "400 MEMBER_CREATED_SCHEDULE_BAD_REQUEST_EXCEPTION / 일정 거절하려는 사람이 Creator 인 경우",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MAASB034",
			description = "400 MEMBER_ALREADY_ACCEPT_SCHEDULE_BAD_REQUEST_EXCEPTION / 이미 일정 수락한 경우",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 해당 Schedule 에 Member 가 없을 때",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse> refuseSchedule(
		@Valid
		@RequestBody RefuseScheduleRequest request
	) {
		scheduleService.refuseSchedule(request.toRefuseScheduleRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}

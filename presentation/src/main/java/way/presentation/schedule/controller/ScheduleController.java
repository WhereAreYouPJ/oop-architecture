package way.presentation.schedule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.schedule.dto.response.SaveScheduleResponseDto;
import way.application.service.schedule.service.ScheduleService;
import way.presentation.base.BaseResponse;
import way.presentation.schedule.vo.req.SaveScheduleRequest;
import way.presentation.schedule.vo.res.SaveScheduleResponse;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	private final ScheduleService scheduleService;

	@PostMapping()
	public ResponseEntity<BaseResponse<SaveScheduleResponse>> saveSchedule(
		@Valid
		@RequestBody SaveScheduleRequest request
	) {
		// VO -> DTO 변환
		SaveScheduleResponseDto saveScheduleResponseDto = scheduleService.createSchedule(request.toScheduleDto());

		// DTO -> VO 변환
		SaveScheduleResponse response = new SaveScheduleResponse(saveScheduleResponseDto.scheduleSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}

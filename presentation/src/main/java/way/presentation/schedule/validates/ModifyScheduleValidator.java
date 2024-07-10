package way.presentation.schedule.validates;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.schedule.vo.req.ModifyScheduleRequest;

@Component
public class ModifyScheduleValidator {
	public void validate(ModifyScheduleRequest request) {
		validateSeq(request.scheduleSeq());
		validateTitle(request.title());
		validateStartTime(request.startTime());
		validateEndTime(request.endTime());
		validateLocation(request.location());
		validateStreetName(request.streetName());
		validateX(request.x());
		validateY(request.y());
		validateColor(request.color());
		validateMemo(request.memo());
		validateCreateMemberSeq(request.createMemberSeq());
	}

	private void validateSeq(Long seq) {
		if (seq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateStartTime(LocalDateTime startTime) {
		if (startTime == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateEndTime(LocalDateTime endTime) {
		if (endTime == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateLocation(String location) {
		if (location == null || location.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateStreetName(String streetName) {
		if (streetName == null || streetName.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateX(Double x) {
		if (x == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateY(Double y) {
		if (y == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateColor(String color) {
		if (color == null || color.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemo(String memo) {
		if (memo == null || memo.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateCreateMemberSeq(Long createMemberSeq) {
		if (createMemberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

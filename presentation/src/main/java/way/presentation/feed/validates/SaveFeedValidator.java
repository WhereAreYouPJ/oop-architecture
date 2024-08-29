package way.presentation.feed.validates;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.request.FeedRequestDto.SaveFeedRequestDto.*;

import java.util.List;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class SaveFeedValidator {
	public void validate(SaveFeedRequestDto request) {
		validateScheduleSeq(request.scheduleSeq());
		validateMemberSeq(request.memberSeq());
		validateTitle(request.title());
		validateImages(request.images());
	}

	private void validateScheduleSeq(Long scheduleSeq) {
		if (scheduleSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.trim().isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateImages(List<ImageInfo> images) {
		if (images == null || images.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

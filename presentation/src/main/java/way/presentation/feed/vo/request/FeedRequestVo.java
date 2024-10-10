package way.presentation.feed.vo.request;

import static way.application.service.feed.dto.request.FeedRequestDto.*;

import java.util.List;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class FeedRequestVo {
	public record SaveFeedRequest(
		Long scheduleSeq,
		Long memberSeq,
		String title,
		String content,
		List<Integer> feedImageOrders
	) {
		public void saveFeedRequestValidate() {
			if (this.scheduleSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.title == null || this.title.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public SaveFeedRequestDto toSaveFeedRequestDto() {
			return new SaveFeedRequestDto(
				this.scheduleSeq,
				this.memberSeq,
				this.title,
				this.content,
				this.feedImageOrders
			);
		}
	}

	public record ModifyFeedRequest(
		Long feedSeq,
		Long memberSeq,
		String title,
		String content,
		List<Integer> feedImageOrders
	) {
		public void modifyFeedRequestValidator() {
			if (this.feedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.title == null || this.title.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public ModifyFeedRequestDto toModifyFeedRequestDto() {
			return new ModifyFeedRequestDto(
				this.feedSeq,
				this.memberSeq,
				this.title,
				this.content,
				this.feedImageOrders
			);
		}
	}

	public record DeleteFeedRequest(
		Long memberSeq,
		Long feedSeq
	) {
		public DeleteFeedRequestDto toDeleteFeedRequestDto() {
			return new DeleteFeedRequestDto(
				this.memberSeq,
				this.feedSeq
			);
		}

		public void validateDeleteFeedRequest() {
			if (this.feedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}
}

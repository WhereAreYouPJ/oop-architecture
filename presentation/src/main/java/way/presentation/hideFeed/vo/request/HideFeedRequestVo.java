package way.presentation.hideFeed.vo.request;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class HideFeedRequestVo {
	public record AddHideFeedRequest(
		Long feedSeq,
		Long memberSeq
	) {
		public AddHideFeedRequestDto toHideFeedRequestDto() {
			return new AddHideFeedRequestDto(
				this.feedSeq,
				this.memberSeq
			);
		}

		public void validateAddHideFeedRequest() {
			if (this.feedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}

	public record DeleteHideFeedRequest(
		Long feedSeq,
		Long memberSeq
	) {
		public DeleteHideFeedRequestDto toDeleteHideFeedRequestDto() {
			return new DeleteHideFeedRequestDto(
				this.feedSeq,
				this.memberSeq
			);
		}

		public void validateDeleteHideFeedRequest() {
			if (this.feedSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}
}

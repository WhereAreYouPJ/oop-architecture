package way.presentation.hideFeed.vo.req;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;

import io.swagger.v3.oas.annotations.media.Schema;

public class HideFeedRequestVo {
	public record HideFeedRequest(
		@Schema(description = "'숨김' 하고자 하는 Feed Seq")
		Long feedSeq,

		@Schema(description = "저장하는 Member Seq")
		Long memberSeq
	) {
		public AddHideFeedRequestDto toHideFeedRequestDto() {
			return new AddHideFeedRequestDto(
				this.feedSeq,
				this.memberSeq
			);
		}
	}

	public record DeleteHideFeedRequest(
		@Schema(description = "Hide Feed PK Seq 값")
		Long hideFeedSeq,
		@Schema(description = "삭제하는 Member Seq")
		Long memberSeq
	) {
		public DeleteHideFeedRequestDto toDeleteHideFeedRequestDto() {
			return new DeleteHideFeedRequestDto(
				this.hideFeedSeq,
				this.memberSeq
			);
		}
	}
}

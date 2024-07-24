package way.presentation.hideFeed.vo.req;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;

public class HideFeedRequestVo {
	public record HideFeedRequest(
		Long scheduleSeq,
		Long memberSeq
	) {
		public AddHideFeedRequestDto toHideFeedRequestDto() {
			return new AddHideFeedRequestDto(
				this.scheduleSeq,
				this.memberSeq
			);
		}
	}
}

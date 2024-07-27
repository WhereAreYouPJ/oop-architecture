package way.presentation.hideFeed.vo.req;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;

public class HideFeedRequestVo {
	public record HideFeedRequest(
		Long hideFeedSeq,
		Long memberSeq
	) {
		public AddHideFeedRequestDto toHideFeedRequestDto() {
			return new AddHideFeedRequestDto(
				this.hideFeedSeq,
				this.memberSeq
			);
		}
	}
}

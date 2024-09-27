package way.presentation.feed.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class FeedResponseVo {
	public record SaveFeedResponse(
		@Schema(description = "저장된 Feed Seq")
		Long feedSeq
	) {

	}

	public record ModifyFeedResponse(
		@Schema(description = "수정된 Feed Seq (초기 생성 Seq와 다릅니다.)")
		Long feedSeq
	) {

	}
}

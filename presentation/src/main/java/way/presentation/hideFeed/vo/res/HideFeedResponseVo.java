package way.presentation.hideFeed.vo.res;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class HideFeedResponseVo {
	public record AddHideFeedResponse(
		@Schema(description = "Hide Feed DB에 저장한 이후 반환되는 Seq 값")
		Long hideFeedSeq
	) {

	}
}

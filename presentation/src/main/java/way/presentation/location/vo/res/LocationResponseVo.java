package way.presentation.location.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationResponseVo {
	public record AddLocationResponse(
		@Schema(description = "Location DB 저장 Seq")
		Long locationSeq
	) {

	}
}

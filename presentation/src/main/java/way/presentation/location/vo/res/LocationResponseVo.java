package way.presentation.location.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationResponseVo {
	public record AddLocationResponse(
		@Schema(description = "Location DB 저장 Seq")
		Long locationSeq
	) {

	}

	public record GetLocationResponse(
		@Schema(description = "Location DB 저장 Seq")
		Long locationSeq,

		@Schema(description = "장소 이름")
		String location,

		@Schema(description = "도로명 주소")
		String streetName
	) {

	}
}

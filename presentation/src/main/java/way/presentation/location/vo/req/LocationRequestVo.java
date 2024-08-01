package way.presentation.location.vo.req;

import static way.application.service.location.dto.request.LocationRequestDto.*;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationRequestVo {
	public record AddLocationRequest(
		@Schema(description = "위치 즐겨찾기 추가하고자 하는 MemberSeq")
		Long memberSeq,

		@Schema(description = "위치 이름 (EX. 스타벅스, 여의도 한강 공원)")
		String location,

		@Schema(description = "실제 도로명 주소")
		String streetName
	) {
		public AddLocationRequestDto toAddLocationRequestDto() {
			return new AddLocationRequestDto(
				this.memberSeq,
				this.location,
				this.streetName
			);
		}
	}

	public record DeleteLocationRequest(
		@Schema(description = "위치 즐겨찾기 삭제하고자 하는 MemberSeq")
		Long memberSeq,
		@Schema(description = "Location DB 저장 시 반환받은 Seqs")
		List<Long> locationSeqs
	) {
		public DeleteLocationRequestDto toDeleteLocationRequestDto() {
			return new DeleteLocationRequestDto(
				this.memberSeq,
				this.locationSeqs
			);
		}
	}
}

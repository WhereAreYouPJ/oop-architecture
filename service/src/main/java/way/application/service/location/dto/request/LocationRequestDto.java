package way.application.service.location.dto.request;

import java.util.List;

public class LocationRequestDto {
	public record AddLocationRequestDto(
		Long memberSeq,
		String location,
		String streetName
	) {

	}

	public record DeleteLocationRequestDto(
		Long memberSeq,
		List<Long> locationSeqs
	) {

	}
}
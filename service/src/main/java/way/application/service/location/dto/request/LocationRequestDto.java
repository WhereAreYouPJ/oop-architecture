package way.application.service.location.dto.request;

public class LocationRequestDto {
	public record AddLocationRequestDto(
		Long memberSeq,
		String location,
		String streetName
	) {

	}
}

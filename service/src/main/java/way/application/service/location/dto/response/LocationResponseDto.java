package way.application.service.location.dto.response;

public class LocationResponseDto {
	public record AddLocationResponseDto(
		Long locationSeq
	) {

	}

	public record GetLocationResponseDto(
		Long locationSeq,
		String location,
		String streetName,
		Long sequence,
		Double x,
		Double y
	) {

	}

	public record ModifyLocationRequestDto(
		Long locationSeq,
		Long sequence
	) {

	}
}

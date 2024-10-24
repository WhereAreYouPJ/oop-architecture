package way.application.service.location.dto.response;

public class LocationResponseDto {
	public record AddLocationResponseDto(
		Long locationSeq
	) {

	}

	public record GetLocationResponseDto(
		Long locationSeq,
		String location,
		String streetName
	) {

	}

	public record ModifyLocationRequestDto(
		Long locationSeq,
		Long sequence
	) {

	}
}

package way.application.service.coordinate.dto.response;

public class CoordinateResponseDto {
	public record GetCoordinateResponseDto(
		Long memberSeq,
		String userName,
		String profileImage,
		Double x,
		Double y
	) {

	}
}

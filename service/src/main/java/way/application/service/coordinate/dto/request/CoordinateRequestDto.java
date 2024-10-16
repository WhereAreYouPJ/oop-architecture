package way.application.service.coordinate.dto.request;

public class CoordinateRequestDto {
	public record CreateCoordinateRequestDto(
		Long memberSeq,
		Double x,
		Double y
	) {

	}
}

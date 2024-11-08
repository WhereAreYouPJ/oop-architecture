package way.application.service.coordinate.dto.request;

public class CoordinateRequestDto {
	public record CreateCoordinateRequestDto(
		Long memberSeq,
		Long scheduleSeq,
		Double x,
		Double y
	) {

	}
}

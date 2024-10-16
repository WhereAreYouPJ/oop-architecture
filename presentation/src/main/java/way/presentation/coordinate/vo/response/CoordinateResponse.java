package way.presentation.coordinate.vo.response;

public class CoordinateResponse {
	public record GetCoordinateResponse(
		Long memberSeq,
		String userName,
		String profileImage,
		Double x,
		Double y
	) {

	}
}

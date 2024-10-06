package way.presentation.location.vo.response;

public class LocationResponseVo {
	public record AddLocationResponse(
		Long locationSeq
	) {

	}

	public record GetLocationResponse(
		Long locationSeq,
		String location,
		String streetName
	) {

	}
}

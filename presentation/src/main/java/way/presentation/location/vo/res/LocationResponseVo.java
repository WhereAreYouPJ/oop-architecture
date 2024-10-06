package way.presentation.location.vo.res;

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

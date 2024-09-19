package way.presentation.admin.vo.response;

public class AdminResponseVo {
	public record AddHomeImageResponse(
		Long adminImageSeq
	) {

	}

	public record GetHomeImageResponse(
		String imageURL
	) {

	}
}

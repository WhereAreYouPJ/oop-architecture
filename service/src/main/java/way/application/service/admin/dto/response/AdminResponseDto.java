package way.application.service.admin.dto.response;

public class AdminResponseDto {
	public record AddHomeImageResponseDto(
		Long adminImageSeq
	) {

	}

	public record GetHomeImageResponseDto(
		String imageURL
	) {

	}
}

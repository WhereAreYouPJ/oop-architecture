package way.application.service.admin.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class AdminRequestDto {
	public record AddHomeImageRequestDto(
		MultipartFile homeImage
	) {

	}

	public record GetHomeImageRequestDto(
		Long adminImageSeq
	) {

	}

	public record DeleteHomeImageRequestDto(
		Long adminImageSeq
	) {

	}
}

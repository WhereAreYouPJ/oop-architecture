package way.presentation.admin.vo.request;

import static way.application.service.admin.dto.request.AdminRequestDto.*;

import org.springframework.web.multipart.MultipartFile;

public class AdminRequestVo {
	public record AddHomeImageRequest(
		MultipartFile homeImage
	) {
		public AddHomeImageRequestDto toAddHomeImageRequestDto() {
			return new AddHomeImageRequestDto(
				this.homeImage
			);
		}
	}

	public record DeleteHomeImageRequest(
		Long adminImageSeq
	) {
		public DeleteHomeImageRequestDto toDeleteHomeImageRequestDto() {
			return new DeleteHomeImageRequestDto(
				this.adminImageSeq
			);
		}
	}
}

package way.presentation.coordinate.vo.request;

import static way.application.service.coordinate.dto.request.CoordinateRequestDto.*;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class CoordinateRequest {
	public record CreateCoordinateRequest(
		Long memberSeq,
		Double x,
		Double y
	) {
		public void validateCreateCoordinateRequest() {
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.x == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.y == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public CreateCoordinateRequestDto toCreateCoordinateRequestDto() {
			return new CreateCoordinateRequestDto(
				this.memberSeq,
				this.x,
				this.y
			);
		}
	}
}

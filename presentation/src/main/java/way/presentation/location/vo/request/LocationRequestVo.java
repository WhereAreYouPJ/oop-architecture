package way.presentation.location.vo.request;

import static way.application.service.location.dto.request.LocationRequestDto.*;
import static way.application.service.location.dto.response.LocationResponseDto.*;

import java.util.List;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

public class LocationRequestVo {
	public record AddLocationRequest(
		Long memberSeq,
		String location,
		String streetName,
		Double x,
		Double y
	) {
		public AddLocationRequestDto toAddLocationRequestDto() {
			return new AddLocationRequestDto(
				this.memberSeq,
				this.location,
				this.streetName,
				this.x,
				this.y
			);
		}

		public void validateAddLocationRequest() {
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.location == null || this.location.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.streetName == null || this.streetName.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.x == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.y == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}

	public record DeleteLocationRequest(
		Long memberSeq,
		List<Long> locationSeqs
	) {
		public DeleteLocationRequestDto toDeleteLocationRequestDto() {
			return new DeleteLocationRequestDto(
				this.memberSeq,
				this.locationSeqs
			);
		}

		public void validateDeleteLocationRequest() {
			if (this.memberSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.locationSeqs == null || this.locationSeqs.isEmpty()) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}
	}

	public record ModifyLocationRequest(
		Long locationSeq,
		Long sequence
	) {
		public void validateModifyLocationRequest() {
			if (this.locationSeq == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
			if (this.sequence == null) {
				throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
			}
		}

		public ModifyLocationRequestDto toModifyLocationRequestDto() {
			return new ModifyLocationRequestDto(
				this.locationSeq,
				this.sequence
			);
		}
	}
}

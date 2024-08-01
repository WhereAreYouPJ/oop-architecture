package way.presentation.location.validates;

import static way.presentation.location.vo.req.LocationRequestVo.*;

import java.util.List;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class DeleteLocationValidator {
	public void validate(DeleteLocationRequest request) {
		validateMemberSeq(request.memberSeq());
		validateLocationSeqs(request.locationSeqs());
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}

	private void validateLocationSeqs(List<Long> locationSeqs) {
		if (locationSeqs == null || locationSeqs.isEmpty()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

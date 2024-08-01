package way.presentation.location.validates;

import static way.presentation.location.vo.req.LocationRequestVo.*;

import org.springframework.stereotype.Component;

import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
public class AddLocationValidator {
	public void validate(AddLocationRequest request) {
		validateMemberSeq(request.memberSeq());
	}

	private void validateMemberSeq(Long memberSeq) {
		if (memberSeq == null) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}

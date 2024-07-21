package way.presentation.Member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.Member.vo.req.MemberRequestVo;

@Component
public class VerifyCodeValidator {

    public void validate(MemberRequestVo.VerifyCodeRequest request) {

        validateEmail(request.email());
        validateCode(request.code());
    }

    private void validateCode(String code) {

        if (code == null || code.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

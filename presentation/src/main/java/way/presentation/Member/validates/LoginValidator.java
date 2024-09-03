package way.presentation.Member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.Member.vo.req.MemberRequestVo;

@Component
public class LoginValidator {

    public void validate(MemberRequestVo.LoginRequest request) {

        validateEmail(request.email());
        validatePassword(request.password());
        validateFcmToken(request.fcmToken());

    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

    private void validateFcmToken(String fcmToken) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

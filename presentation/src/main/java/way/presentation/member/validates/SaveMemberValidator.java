package way.presentation.member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.member.vo.req.MemberRequestVo.SaveMemberRequest;

@Component
public class SaveMemberValidator {

    public void validate(SaveMemberRequest request) {

        validateUserName(request.userName());
        validateEmail(request.email());
        validatePassword(request.password());

    }

    private void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
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


}

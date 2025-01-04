package way.presentation.member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

import static way.presentation.member.vo.req.MemberRequestVo.*;

@Component
public class SendEmailRequestValidator {

    public void validate(MailSendRequest request) {

        validateEmail(request.email());
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

package way.presentation.member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.member.vo.req.MemberRequestVo;

@Component
public class ResetPasswordValidator {

    public void validate(MemberRequestVo.PasswordResetRequest request) {

        validateDto(request.email());
        validateDto(request.password());
        validateDto(request.checkPassword());

    }

    private void validateDto(String dto) {
        if (dto == null || dto.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

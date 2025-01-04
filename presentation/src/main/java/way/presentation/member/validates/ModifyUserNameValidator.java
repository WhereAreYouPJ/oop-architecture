package way.presentation.member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.member.vo.req.MemberRequestVo;

@Component
public class ModifyUserNameValidator {

    public void validate(MemberRequestVo.ModifyUserNameRequest request) {

        validateUserName(request.userName());
        validateMemberSeq(request.memberSeq());

    }

    private void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

    private void validateMemberSeq(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

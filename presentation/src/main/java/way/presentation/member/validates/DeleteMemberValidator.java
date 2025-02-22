package way.presentation.member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

import static way.presentation.member.vo.req.MemberRequestVo.*;

@Component
public class DeleteMemberValidator {

    public void validate(DeleteMemberRequest request) {

        validateMemberSeq(request.memberSeq());
        validateLoginType(request.loginType());
    }

    private void validateLoginType(String loginType) {
        if (loginType == null || loginType.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

    private void validateMemberSeq(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

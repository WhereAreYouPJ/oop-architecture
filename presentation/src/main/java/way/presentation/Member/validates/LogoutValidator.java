package way.presentation.Member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.Member.vo.req.MemberRequestVo;

@Component
public class LogoutValidator {

    public void validate(MemberRequestVo.LogoutRequest request) {

        validateMemberSeq(request.memberSeq());

    }

    private void validateMemberSeq(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

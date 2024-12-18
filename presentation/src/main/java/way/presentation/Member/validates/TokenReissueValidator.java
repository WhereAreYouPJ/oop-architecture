package way.presentation.Member.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.Member.vo.req.MemberRequestVo;

@Component
public class TokenReissueValidator {

    public void validate(MemberRequestVo.TokenReissueRequest request) {

        validateRefreshToken(request.refreshToken());
    }

    private void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

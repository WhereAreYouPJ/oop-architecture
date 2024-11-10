package way.presentation.friendRequest.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.friendRequest.vo.req.FriendRequestVo;

import static way.presentation.friendRequest.vo.req.FriendRequestVo.*;

@Component
public class CancelValidator {

    public void validate(Cancel request) {

        validateDto(request.friendRequestSeq());

    }

    private void validateDto(Long dtoReq) {
        if (dtoReq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

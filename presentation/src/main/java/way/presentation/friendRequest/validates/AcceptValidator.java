package way.presentation.friendRequest.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.friendRequest.vo.req.FriendRequestVo;
@Component
public class AcceptValidator {

    public void validate(FriendRequestVo.Accept request) {

        validateDto(request.memberSeq());
        validateDto(request.senderSeq());
        validateDto(request.friendRequestSeq());

    }

    private void validateDto(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

package way.presentation.friendRequest.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.friendRequest.vo.req.FriendRequestVo;

@Component

public class SaveFriendRequestValidator {

    public void validate(FriendRequestVo.SaveFriendRequestRequest request) {

        validateMemberSeq(request.memberSeq());
        validateMemberSeq(request.friendSeq());

    }

    private void validateMemberSeq(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }

}

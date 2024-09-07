package way.presentation.friend.validates;

import org.springframework.stereotype.Component;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;
import way.presentation.friend.vo.req.FriendVo;

@Component
public class AddFavoritesValidator {

    public void validate(FriendVo.AddFavorites request) {

        validateDto(request.memberSeq());
        validateDto(request.friendSeq());

    }

    private void validateDto(Long memberSeq) {
        if (memberSeq == null) {
            throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
        }
    }
}

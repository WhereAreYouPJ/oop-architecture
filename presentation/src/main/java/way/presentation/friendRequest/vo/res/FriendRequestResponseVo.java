package way.presentation.friendRequest.vo.res;

import java.time.LocalDateTime;

public class FriendRequestResponseVo {


    public record GetFriendRequestListResponse(
            Long friendRequestSeq,
            Long senderSeq,
            LocalDateTime createTime
    ) {

    }
}

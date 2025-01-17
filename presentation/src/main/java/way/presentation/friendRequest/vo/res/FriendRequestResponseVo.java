package way.presentation.friendRequest.vo.res;

import java.time.LocalDateTime;

public class FriendRequestResponseVo {


    public record GetFriendRequestedListResponse(
            Long friendRequestSeq,
            Long senderSeq,
            LocalDateTime createTime,
            String profileImage,
            String userName,
            String memberCode
    ) {

    }

    public record GetFriendRequestListResponse(
            Long friendRequestSeq,
            Long receiverSeq,
            LocalDateTime createTime,
            String profileImage,
            String userName
    ) {

    }
}

package way.presentation.friend.vo.res;

public class FriendResponseVo {

    public record GetFriendListResponse(
            Long memberSeq,
            String userName,
            String profileImage
    ) {

    }

}

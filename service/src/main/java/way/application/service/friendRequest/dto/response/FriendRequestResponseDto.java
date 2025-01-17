package way.application.service.friendRequest.dto.response;

import java.time.LocalDateTime;

public class FriendRequestResponseDto {

    public record FriendRequestedList(
            Long friendRequestSeq,
            Long senderSeq,
            LocalDateTime createTime,
            String profileImage,
            String userName,
            String memberCode
    ) {

    }

    public record FriendRequestList(
            Long friendRequestSeq,
            Long receiverSeq,
            LocalDateTime createTime,
            String profileImage,
            String userName
    ) {

    }

}

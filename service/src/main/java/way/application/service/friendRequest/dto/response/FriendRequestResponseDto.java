package way.application.service.friendRequest.dto.response;

import java.time.LocalDateTime;

public class FriendRequestResponseDto {

    public record FriendRequestList(
            Long friendRequestSeq,
            Long senderSeq,
            LocalDateTime createTime
    ) {

    }

}

package way.application.service.friendRequest.dto.request;

import java.time.LocalDateTime;

public class FriendRequestDto {
    public record SaveFriendRequestDto(
            // 친추 보낼 아이디
            Long memberSeq,
            // 친추 보낸 아이디
            Long friendSeq
    ) {

    }

    public record GetFriendRequestListDto(
            Long memberSeq
    ) {

    }

    public record AcceptDto(
            Long friendRequestSeq,
            Long memberSeq,
            Long senderSeq
    ) {

    }

    public record RefuseDto(
            Long friendRequestSeq
    ) {

    }

    public record CancelDto(
            Long friendRequestSeq
    ) {

    }
}

package way.application.service.friend.dto.request;

public class FriendDto {

    public record GetFriendListDto(
            Long memberSeq
    ) {

    }

    public record DeleteFriendDto(
            Long memberSeq,
            Long friendSeq
    ) {
    }
}

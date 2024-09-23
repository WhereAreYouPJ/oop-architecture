package way.application.service.friend.dto.response;

public class FriendResponseDto {

    public record GetFriendList(
            Long memberSeq,
            String userName,
            String profileImage,
            boolean favorites
    ) {

    }

}

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

    public record AddFavoritesDto(
            Long friendSeq,
            Long memberSeq
    ) {
    }

    public record RemoveFavoritesDto(
            Long friendSeq,
            Long memberSeq
    ) {
    }
}

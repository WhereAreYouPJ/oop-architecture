package way.presentation.friend.vo.req;

import way.application.service.friend.dto.request.FriendDto;

public class FriendVo {

    public record GetFriendList(

            Long memberSeq
    ) {
        public FriendDto.GetFriendListDto toGetFriendList() {
            return new FriendDto.GetFriendListDto(
                    this.memberSeq
            );
        }
    }

    public record DeleteFriend(

            Long memberSeq,

            Long friendSeq

    ) {

        public FriendDto.DeleteFriendDto toDeleteFriend() {
            return new FriendDto.DeleteFriendDto(
                    this.memberSeq,
                    this.friendSeq
            );
        }
    }

    public record AddFavorites(
            Long friendSeq,
            Long memberSeq
    ) {
        public FriendDto.AddFavoritesDto toAddFavorites() {
            return new FriendDto.AddFavoritesDto(
                    this.friendSeq,
                    this.memberSeq
            );
        }
    }

    public record RemoveFavorites(
            Long friendSeq,
            Long memberSeq
    ) {
        public FriendDto.RemoveFavoritesDto toRemoveFavorites() {
            return new FriendDto.RemoveFavoritesDto(
                    this.friendSeq,
                    this.memberSeq
            );
        }
    }


}

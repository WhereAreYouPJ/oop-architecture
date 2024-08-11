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


}

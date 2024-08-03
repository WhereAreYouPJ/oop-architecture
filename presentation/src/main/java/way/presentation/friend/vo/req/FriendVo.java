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


}

package way.presentation.friendRequest.vo.req;

import way.application.service.friendRequest.dto.request.FriendRequestDto;

import java.time.LocalDateTime;

public class FriendRequestVo {

    public record SaveFriendRequestRequest(
            // 친추 보낼 아이디
            Long memberSeq,
            // 친추 보낸 아이디
            Long friendSeq,
            LocalDateTime localDateTime
    ) {
        public FriendRequestDto.SaveFriendRequestDto toSaveFriendRequestDto() {
            return new FriendRequestDto.SaveFriendRequestDto(
                    this.memberSeq,
                    this.friendSeq,
                    this.localDateTime
            );
        }
    }

    public record GetFriendRequestList(
            Long memberSeq
    ) {
        public FriendRequestDto.GetFriendRequestListDto toGetFriendRequestList() {
            return new FriendRequestDto.GetFriendRequestListDto(
                    this.memberSeq
            );
        }
    }

    public record Accept(
            Long friendRequestSeq,
            Long memberSeq,
            Long senderSeq

    ) {
        public FriendRequestDto.AcceptDto toAccept() {
            return new FriendRequestDto.AcceptDto(
                    this.friendRequestSeq,
                    this.memberSeq,
                    this.senderSeq
            );
        }

    }

}

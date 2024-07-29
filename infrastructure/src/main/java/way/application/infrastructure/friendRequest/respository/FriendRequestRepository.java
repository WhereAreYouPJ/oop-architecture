package way.application.infrastructure.friendRequest.respository;

import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;

public interface FriendRequestRepository {
    MemberEntity validateSenderSeq(Long memberSeq);

    MemberEntity validateReceiverSeq(Long receiverSeq);

    void validateMemberAndFriend(Long memberSeq, Long receiverSeq);

    void validateAlreadyFriendRequest(MemberEntity sender, MemberEntity receiver);

    void validateAlreadyFriendRequestByFriend(MemberEntity receiver, MemberEntity sender);

    void saveFriendRequest(FriendRequestEntity friendRequestEntity);
}
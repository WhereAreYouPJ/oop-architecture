package way.application.infrastructure.friendRequest.respository;

import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;

import java.util.List;

public interface FriendRequestRepository {
    MemberEntity validateSenderSeq(Long memberSeq);

    MemberEntity validateReceiverSeq(Long receiverSeq);

    void validateMemberAndFriend(Long memberSeq, Long receiverSeq);

    void validateAlreadyFriendRequest(MemberEntity sender, MemberEntity receiver);

    void validateAlreadyFriendRequestByFriend(MemberEntity receiver, MemberEntity sender);

    void saveFriendRequest(FriendRequestEntity friendRequestEntity);

    MemberEntity validateMemberSeq(Long memberSeq);

    List<FriendRequestEntity> findFriendRequestByMemberSeq(MemberEntity memberEntity);

    FriendRequestEntity findFriendRequestById(Long friendRequestSeq);

    void delete(FriendRequestEntity friendRequest);
}

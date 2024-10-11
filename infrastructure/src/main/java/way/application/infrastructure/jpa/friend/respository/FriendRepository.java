package way.application.infrastructure.jpa.friend.respository;

import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

import java.util.List;

public interface FriendRepository {
    void saveFriend(FriendEntity friendEntity);

    MemberEntity validateMemberSeq(Long memberSeq);

    List<FriendEntity> findByOwner(MemberEntity member);

    void delete(MemberEntity member, MemberEntity friend);

    FriendEntity findByOwnerAndFriend(MemberEntity member, MemberEntity friend);

    void deleteAllByMemberSeq(MemberEntity memberEntity);
}

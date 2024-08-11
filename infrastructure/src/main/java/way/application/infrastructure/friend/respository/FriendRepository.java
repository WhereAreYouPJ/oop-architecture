package way.application.infrastructure.friend.respository;

import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.member.entity.MemberEntity;

import java.util.List;

public interface FriendRepository {
    void saveFriend(FriendEntity friendEntity);

    MemberEntity validateMemberSeq(Long memberSeq);

    List<FriendEntity> findByOwner(MemberEntity member);

    void delete(MemberEntity member, MemberEntity friend);
}

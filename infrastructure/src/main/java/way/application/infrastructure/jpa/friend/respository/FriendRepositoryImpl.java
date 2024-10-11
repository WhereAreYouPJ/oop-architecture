package way.application.infrastructure.jpa.friend.respository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.friend.entity.QFriendEntity;
import way.application.infrastructure.jpa.friendRequest.entity.QFriendRequestEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberJpaRepository;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendJpaRepository friendJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public void saveFriend(FriendEntity friendEntity) {
        friendJpaRepository.save(friendEntity);
    }

    @Override
    public MemberEntity validateMemberSeq(Long memberSeq) {
        return memberJpaRepository.findById(memberSeq)
                .orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_BAD_REQUEST_EXCEPTION));
    }

    @Override
    public List<FriendEntity> findByOwner(MemberEntity member) {
        return friendJpaRepository.findByOwnerOrderByFriendsUserName(member);

    }

    @Override
    public void delete(MemberEntity member, MemberEntity friend) {
        friendJpaRepository.deleteByOwnerAndFriends(member,friend);
    }

    @Override
    public FriendEntity findByOwnerAndFriend(MemberEntity member, MemberEntity friend) {
        return friendJpaRepository.findByOwnerAndFriends(member,friend)
                .orElseThrow(() -> new BadRequestException(ErrorResult.FRIEND_NOT_FOUND_EXCEPTION));

    }

    @Override
    public void deleteAllByMemberSeq(MemberEntity memberEntity) {
        QFriendEntity friendEntity = QFriendEntity.friendEntity;

        queryFactory
                .delete(friendEntity)
                .where(
                        friendEntity.friends.eq(memberEntity)
                                .or(friendEntity.owner.eq(memberEntity))
                ).execute();

        em.flush();
        em.clear();
    }
}

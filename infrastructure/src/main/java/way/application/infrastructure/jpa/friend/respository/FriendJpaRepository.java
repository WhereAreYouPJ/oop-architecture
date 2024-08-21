package way.application.infrastructure.jpa.friend.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

import java.util.List;

@Repository
public interface FriendJpaRepository extends JpaRepository<FriendEntity, Long> {

        List<FriendEntity> findByOwner(MemberEntity member);

        @Transactional
        void deleteByOwnerAndFriends(MemberEntity member, MemberEntity friend);

}

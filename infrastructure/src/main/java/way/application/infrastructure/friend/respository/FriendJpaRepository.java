package way.application.infrastructure.friend.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.member.entity.MemberEntity;

import java.util.List;

@Repository
public interface FriendJpaRepository extends JpaRepository<FriendEntity, Long> {

        List<FriendEntity> findByOwner(MemberEntity member);
}

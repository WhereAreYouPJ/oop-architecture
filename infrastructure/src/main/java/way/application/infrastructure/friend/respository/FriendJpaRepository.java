package way.application.infrastructure.friend.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import way.application.infrastructure.friend.entity.FriendEntity;

@Repository
public interface FriendJpaRepository extends JpaRepository<FriendEntity, Long> {


}

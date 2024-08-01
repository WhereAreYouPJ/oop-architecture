package way.application.infrastructure.friend.respository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.friend.entity.FriendEntity;

@Component
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendJpaRepository friendJpaRepository;

    @Override
    public void saveFriend(FriendEntity friendEntity) {
        friendJpaRepository.save(friendEntity);
    }
}

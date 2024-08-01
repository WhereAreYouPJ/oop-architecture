package way.application.infrastructure.friend.respository;

import way.application.infrastructure.friend.entity.FriendEntity;

public interface FriendRepository {
    void saveFriend(FriendEntity friendEntity);
}

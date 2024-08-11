package way.application.infrastructure.friend.respository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberJpaRepository;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendJpaRepository friendJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

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
        return friendJpaRepository.findByOwner(member);

    }

    @Override
    public void delete(MemberEntity member, MemberEntity friend) {
        friendJpaRepository.deleteByOwnerAndFriends(member,friend);
    }
}

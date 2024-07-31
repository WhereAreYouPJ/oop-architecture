package way.application.infrastructure.friendRequest.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;

import java.util.List;

@Repository
public interface FriendRequestJpaRepository extends JpaRepository<FriendRequestEntity, Long> {

    boolean existsByReceiverSeqAndSenderSeq(MemberEntity senderSeq, MemberEntity receiverSeq);

    List<FriendRequestEntity> findByReceiverSeq(MemberEntity member);
}

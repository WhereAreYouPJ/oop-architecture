package way.application.infrastructure.jpa.friendRequest.respository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.jpa.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.jpa.friendRequest.entity.QFriendRequestEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberJpaRepository;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendRequestRepositoryImpl implements FriendRequestRepository{

    private final MemberJpaRepository memberJpaRepository;
    private final FriendRequestJpaRepository friendRequestJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public MemberEntity validateSenderSeq(Long memberSeq) {

        return memberJpaRepository.findById(memberSeq)
                .orElseThrow(() -> new BadRequestException(ErrorResult.SENDER_SEQ_BAD_REQUEST_EXCEPTION));

    }

    @Override
    public MemberEntity validateReceiverSeq(Long receiverSeq) {

        return memberJpaRepository.findById(receiverSeq)
                .orElseThrow(() -> new BadRequestException(ErrorResult.RECEIVER_SEQ_BAD_REQUEST_EXCEPTION));
    }

    @Override
    public void validateMemberAndFriend(Long memberSeq, Long receiverSeq) {

        if(memberSeq.equals(receiverSeq)) {
            throw new BadRequestException(ErrorResult.SELF_FRIEND_REQUEST_BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public void validateAlreadyFriendRequest(MemberEntity sender, MemberEntity receiver) {

        if(friendRequestJpaRepository.existsByReceiverSeqAndSenderSeq(sender, receiver)) {
            throw new BadRequestException(ErrorResult.ALREADY_SENT_BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public void validateAlreadyFriendRequestByFriend(MemberEntity receiver, MemberEntity sender) {

        if (friendRequestJpaRepository.existsByReceiverSeqAndSenderSeq(receiver,sender)) {
            throw new BadRequestException(ErrorResult.ALREADY_SENT_BY_FRIEND_BAD_REQUEST_EXCEPTION);
        }

    }

    @Override
    public void saveFriendRequest(FriendRequestEntity friendRequestEntity) {
        friendRequestJpaRepository.save(friendRequestEntity);
    }

    @Override
    public MemberEntity validateMemberSeq(Long memberSeq) {
        return memberJpaRepository.findById(memberSeq)
                .orElseThrow(() -> new BadRequestException(ErrorResult.MEMBER_SEQ_BAD_REQUEST_EXCEPTION));
    }

    @Override
    public List<FriendRequestEntity> findFriendRequestByReceiverSeq(MemberEntity memberEntity) {
        return friendRequestJpaRepository.findByReceiverSeq(memberEntity);

    }

    @Override
    public List<FriendRequestEntity> findFriendRequestBySenderSeq(MemberEntity memberEntity) {
        return friendRequestJpaRepository.findBySenderSeq(memberEntity);

    }

    @Override
    public FriendRequestEntity findFriendRequestById(Long friendRequestSeq) {
        return friendRequestJpaRepository.findById(friendRequestSeq)
                .orElseThrow(() -> new BadRequestException(ErrorResult.FRIENDREQUEST_SEQ_BAD_REQUEST_EXCEPTION));
    }

    @Override
    public void delete(FriendRequestEntity friendRequest) {
        friendRequestJpaRepository.delete(friendRequest);
    }

    @Override
    public void deleteAllByMemberSeq(MemberEntity memberSeq) {

        QFriendRequestEntity friendRequestEntity = QFriendRequestEntity.friendRequestEntity;

        queryFactory
                .delete(friendRequestEntity)
                .where(
                        friendRequestEntity.senderSeq.eq(memberSeq)
                                .or(friendRequestEntity.receiverSeq.eq(memberSeq))
                ).execute();

        em.flush();
        em.clear();

    }
}

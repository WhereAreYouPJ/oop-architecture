package way.application.infrastructure.friendRequest.respository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberJpaRepository;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class FriendRequestRepositoryImpl implements FriendRequestRepository{

    private final MemberJpaRepository memberJpaRepository;
    private final FriendRequestJpaRepository friendRequestJpaRepository;

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
}

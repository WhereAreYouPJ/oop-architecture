package way.application.domain.friendRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;


@Component
@RequiredArgsConstructor
public class FriendRequestDomain {


    public void validateSenderSeq(MemberEntity FriendRequestSender, MemberEntity sender) {

        if(FriendRequestSender != sender) {
            throw new BadRequestException(ErrorResult.SENDER_SEQ_MISMATCH_REQUEST_EXCEPTION);
        }

    }

    public void validateReceiverSeq(MemberEntity FriendRequestReceiver, MemberEntity receiver) {

        if(FriendRequestReceiver != receiver) {
            throw new BadRequestException(ErrorResult.RECEIVER_SEQ_MISMATCH_REQUEST_EXCEPTION);
        }
    }
}

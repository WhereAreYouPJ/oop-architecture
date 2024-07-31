package way.application.service.friendRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.friendRequest.respository.FriendRequestRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.service.friendRequest.dto.request.FriendRequestDto;
import way.application.service.friendRequest.dto.response.FriendRequestResponseDto;
import way.application.service.friendRequest.mapper.FriendRequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestMapper friendRequestMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final FirebaseNotificationDomain firebaseNotificationDomain;

    @Transactional
    public void saveFriendRequest(FriendRequestDto.SaveFriendRequestDto saveFriendRequestDto) {

        // 신청 보낸 멤버 유효성 검사
        MemberEntity sender = friendRequestRepository.validateSenderSeq(saveFriendRequestDto.memberSeq());

        // 신청 받은 멤버 유효성 검사
        MemberEntity receiver = friendRequestRepository.validateReceiverSeq(saveFriendRequestDto.friendSeq());

        // 본인 한테 친구 요청 보낼 수 없음
        friendRequestRepository.validateMemberAndFriend(saveFriendRequestDto.memberSeq(), saveFriendRequestDto.friendSeq());

        // 이미 친구 요청 전송됨
        friendRequestRepository.validateAlreadyFriendRequest(sender, receiver);

        //친구가 친구 요청을 보냄
        friendRequestRepository.validateAlreadyFriendRequestByFriend(receiver, sender);

        //이미 친구 ( 추후 예정)

        // 친구 요청 저장
        friendRequestRepository.saveFriendRequest(
                friendRequestMapper.toFriendRequestEntity(sender, receiver, LocalDateTime.now())
        );

        // 푸시 알림
        firebaseNotificationDomain.sendFriendRequestNotification(receiver,sender);

    }

    public List<FriendRequestResponseDto.FriendRequestList> getFriendRequestList(FriendRequestDto.GetFriendRequestList getFriendRequestList) {

        // 멤버 조회
        MemberEntity memberEntity = friendRequestRepository.validateMemberSeq(getFriendRequestList.memberSeq());

        // 친구 요청 조회
        List<FriendRequestEntity> friendRequestList = friendRequestRepository.findFriendRequestByMemberSeq(memberEntity);

        return friendRequestList.stream().map(friendRequestEntity -> new FriendRequestResponseDto.FriendRequestList(
                friendRequestEntity.getFriendRequestSeq(),
                friendRequestEntity.getSenderSeq().getMemberSeq(),
                friendRequestEntity.getCreateTime())).collect(Collectors.toList());

    }
}

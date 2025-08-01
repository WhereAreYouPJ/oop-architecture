package way.application.service.friendRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.domain.firebase.FirebaseNotificationDomain;
import way.application.domain.friendRequest.FriendRequestDomain;
import way.application.infrastructure.jpa.friend.respository.FriendRepository;
import way.application.infrastructure.jpa.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.jpa.friendRequest.respository.FriendRequestRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.service.friend.mapper.FriendMapper;
import way.application.service.friendRequest.mapper.FriendRequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static way.application.service.friendRequest.dto.request.FriendRequestDto.*;
import static way.application.service.friendRequest.dto.response.FriendRequestResponseDto.*;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestMapper friendRequestMapper;
    private final FriendMapper friendMapper;
    private final FriendRequestRepository friendRequestRepository;
    private final FirebaseNotificationDomain firebaseNotificationDomain;
    private final FriendRepository friendRepository;
    private final FriendRequestDomain friendRequestDomain;

    @Transactional
    public void saveFriendRequest(SaveFriendRequestDto saveFriendRequestDto) {

        // 신청 보낸 멤버 유효성 검사
        MemberEntity sender = friendRequestRepository.validateSenderSeq(saveFriendRequestDto.memberSeq());

        // 신청 받은 멤버 유효성 검사
        MemberEntity receiver = friendRequestRepository.validateReceiverSeq(saveFriendRequestDto.friendSeq());

        //이미 친구
        friendRepository.validateAlreadyFriend(sender, receiver);

        // 본인 한테 친구 요청 보낼 수 없음
        friendRequestRepository.validateMemberAndFriend(saveFriendRequestDto.memberSeq(), saveFriendRequestDto.friendSeq());

        // 이미 친구 요청 전송됨
        friendRequestRepository.validateAlreadyFriendRequest(sender, receiver);

        //친구가 친구 요청을 보냄
        friendRequestRepository.validateAlreadyFriendRequestByFriend(receiver, sender);

        // 친구 요청 저장
        friendRequestRepository.saveFriendRequest(
                friendRequestMapper.toFriendRequestEntity(sender, receiver, LocalDateTime.now())
        );

        // 푸시 알림
        firebaseNotificationDomain.sendFriendRequestNotification(receiver,sender);

    }

    public List<FriendRequestedList> getFriendRequestedList(GetFriendRequestListDto getFriendRequestListDto) {

        // 멤버 조회
        MemberEntity memberEntity = friendRequestRepository.validateMemberSeq(getFriendRequestListDto.memberSeq());

        // 친구 요청 조회
        List<FriendRequestEntity> friendRequestList = friendRequestRepository.findFriendRequestByReceiverSeq(memberEntity);

        return friendRequestList.stream().map(friendRequestEntity -> new FriendRequestedList(
                friendRequestEntity.getFriendRequestSeq(),
                friendRequestEntity.getSenderSeq().getMemberSeq(),
                friendRequestEntity.getCreateTime(),
                friendRequestEntity.getSenderSeq().getProfileImage(),
                friendRequestEntity.getSenderSeq().getUserName(),
                friendRequestEntity.getSenderSeq().getMemberCode()))
                .collect(Collectors.toList());

    }

    public List<FriendRequestList> getFriendRequestList(GetFriendRequestListDto getFriendRequestListDto) {

        // 멤버 조회
        MemberEntity memberEntity = friendRequestRepository.validateMemberSeq(getFriendRequestListDto.memberSeq());

        // 친구 요청 조회
        List<FriendRequestEntity> friendRequestList = friendRequestRepository.findFriendRequestBySenderSeq(memberEntity);

        return friendRequestList.stream().map(friendRequestEntity -> new FriendRequestList(
                        friendRequestEntity.getFriendRequestSeq(),
                        friendRequestEntity.getReceiverSeq().getMemberSeq(),
                        friendRequestEntity.getCreateTime(),
                        friendRequestEntity.getReceiverSeq().getProfileImage(),
                        friendRequestEntity.getReceiverSeq().getUserName()))
                .collect(Collectors.toList());

    }

    @Transactional
    public void accept(AcceptDto acceptDto) {

        // 조회
        MemberEntity member = friendRequestRepository.validateMemberSeq(acceptDto.memberSeq());
        MemberEntity sender = friendRequestRepository.validateMemberSeq(acceptDto.senderSeq());

        // 친구 요청 확인
        FriendRequestEntity friendRequest = friendRequestRepository.findFriendRequestById(acceptDto.friendRequestSeq());

        // SENDER 확인
        friendRequestDomain.validateSenderSeq(friendRequest.getSenderSeq(), sender);

        // RECEIVER 확인
        friendRequestDomain.validateReceiverSeq(friendRequest.getReceiverSeq(), member);

        //친구 저장
        friendRepository.saveFriend(
                friendMapper.toFriendEntity(member,sender)
        );

        friendRepository.saveFriend(
                friendMapper.toFriendEntity(sender,member)
        );

        //친구 요청 삭제
        friendRequestRepository.delete(friendRequest);

        // 푸시 알림
        firebaseNotificationDomain.acceptNotification(sender,member);

    }

    @Transactional
    public void refuse(RefuseDto refuseDto) {

        // 친구 요청 확인
        FriendRequestEntity friendRequest = friendRequestRepository.findFriendRequestById(refuseDto.friendRequestSeq());

        // 친규 요청 삭제
        friendRequestRepository.delete(friendRequest);

    }

    @Transactional
    public void cancel(CancelDto cancelDto) {

        // 친구 요청 확인
        FriendRequestEntity friendRequest = friendRequestRepository.findFriendRequestById(cancelDto.friendRequestSeq());

        // 친규 요청 삭제
        friendRequestRepository.delete(friendRequest);

    }
}

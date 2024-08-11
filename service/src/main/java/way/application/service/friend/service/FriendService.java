package way.application.service.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.friend.respository.FriendRepository;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.friend.dto.request.FriendDto;
import way.application.service.friend.dto.response.FriendResponseDto;
import way.application.service.friendRequest.dto.response.FriendRequestResponseDto;

import java.util.List;
import java.util.stream.Collectors;

import static way.application.service.friend.dto.request.FriendDto.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;

    public List<FriendResponseDto.GetFriendList> getFriendList(GetFriendListDto getFriendListDto) {

        MemberEntity member = memberRepository.findByMemberSeq(getFriendListDto.memberSeq());

        List<FriendEntity> friendList = friendRepository.findByOwner(member);


        return friendList.stream().map(friendEntity -> new FriendResponseDto.GetFriendList(
                friendEntity.getFriends().getMemberSeq(),
                friendEntity.getFriends().getUserName(),
                friendEntity.getFriends().getProfileImage())).collect(Collectors.toList());

    }

    @Transactional
    public void delete(DeleteFriendDto deleteFriendDto) {

        MemberEntity member = memberRepository.findByMemberSeq(deleteFriendDto.memberSeq());
        MemberEntity friend = memberRepository.findByMemberSeq(deleteFriendDto.friendSeq());

        // 친구 삭제
        friendRepository.delete(member, friend);
        friendRepository.delete(friend, member);

    }
}

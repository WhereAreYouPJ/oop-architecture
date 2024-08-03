package way.application.service.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import way.application.infrastructure.friend.entity.FriendEntity;
import way.application.infrastructure.friend.respository.FriendRepository;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.service.friend.dto.request.FriendDto;
import way.application.service.friend.dto.response.FriendResponseDto;
import way.application.service.friendRequest.dto.response.FriendRequestResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public List<FriendResponseDto.GetFriendList> getFriendList(FriendDto.GetFriendListDto getFriendListDto) {

        MemberEntity member = friendRepository.validateMemberSeq(getFriendListDto.memberSeq());

        List<FriendEntity> friendList = friendRepository.findByOwner(member);


        return friendList.stream().map(friendEntity -> new FriendResponseDto.GetFriendList(
                friendEntity.getFriends().getMemberSeq(),
                friendEntity.getFriends().getUserName(),
                friendEntity.getFriends().getProfileImage())).collect(Collectors.toList());

    }
}

package way.application.domain.friend;

import java.util.List;

import org.springframework.stereotype.Component;

import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.ErrorResult;
import way.application.utils.exception.NotFoundRequestException;

@Component
public class FriendDomain {
	public void checkFriends(List<MemberEntity> memberEntities, List<FriendEntity> friendEntities) {
		List<Long> invitedMemberSeqs = memberEntities.stream()
			.map(MemberEntity::getMemberSeq)
			.toList();

		boolean friend = invitedMemberSeqs.stream()
			.allMatch(invitedMemberSeq ->
				friendEntities.stream()
					.anyMatch(friendEntity ->
						friendEntity.getFriends().getMemberSeq().equals(invitedMemberSeq)
					)
			);

		if (!friend)
			throw new NotFoundRequestException(ErrorResult.FRIEND_NOT_FOUND_EXCEPTION);
	}
}

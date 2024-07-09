package way.application.domain.member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import way.application.infrastructure.member.entity.MemberEntity;

@Component
public class MemberDomain {

	public Set<MemberEntity> createMemberSet(MemberEntity createMemberEntity, List<MemberEntity> invitedMemberEntity) {
		Set<MemberEntity> totalMemberEntity = new HashSet<>(invitedMemberEntity);
		totalMemberEntity.add(createMemberEntity);

		return totalMemberEntity;
	}

	public boolean checkIsCreator(MemberEntity invitedMember, MemberEntity createMemberEntity) {
		return invitedMember.getMemberSeq().equals(createMemberEntity.getMemberSeq());
	}
}

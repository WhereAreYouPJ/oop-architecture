package way.application.domain.member;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import way.application.infrastructure.member.entity.MemberEntity;

@Component
public class MemberDomain {

	/**
	 * @param createMemberEntity
	 * @param invitedMemberEntity
	 *
	 * 일정 추가 시 일정에 들어가는 MemberEntity 구성
	 * create(일정 생성자) + invited(일정 초대자) Set 구성 후 반환
	 */
	public Set<MemberEntity> createMemberSet(MemberEntity createMemberEntity, List<MemberEntity> invitedMemberEntity) {
		Set<MemberEntity> totalMemberEntity = new HashSet<>(invitedMemberEntity);
		totalMemberEntity.add(createMemberEntity);

		return totalMemberEntity;
	}

	/**
	 * @param invitedMemberEntity
	 * @param createMemberEntity
	 *
	 * 일정 초대자 <-> 일정 생성자 동일 여부 확인
	 * Seq 값 비교 처리
	 */
	public boolean checkIsCreator(MemberEntity invitedMemberEntity, MemberEntity createMemberEntity) {
		return invitedMemberEntity.getMemberSeq().equals(createMemberEntity.getMemberSeq());
	}
}

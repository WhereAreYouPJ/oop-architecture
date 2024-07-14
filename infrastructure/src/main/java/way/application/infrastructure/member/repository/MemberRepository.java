package way.application.infrastructure.member.repository;

import java.util.List;

import way.application.infrastructure.member.entity.MemberEntity;

public interface MemberRepository {
	// Member Seq Validation 메서드
	MemberEntity findByMemberSeq(Long memberSeq);

	void isDuplicatedUserId(String userId);

	// Member Seqs Validation 메서드
	List<MemberEntity> findByMemberSeqs(List<Long> memberSeqs);

	MemberEntity saveMember(MemberEntity memberEntity);
}

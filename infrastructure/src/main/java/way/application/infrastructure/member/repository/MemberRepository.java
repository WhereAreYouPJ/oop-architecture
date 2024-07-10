package way.application.infrastructure.member.repository;

import java.util.List;

import way.application.infrastructure.member.entity.MemberEntity;

public interface MemberRepository {
	// Member Seq Validation 메서드
	MemberEntity validateMemberSeq(Long memberSeq);

	// Member Seqs Validation 메서드
	List<MemberEntity> validateMemberSeqs(List<Long> memberSeqs);
}

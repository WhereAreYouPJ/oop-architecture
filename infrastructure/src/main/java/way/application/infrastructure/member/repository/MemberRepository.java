package way.application.infrastructure.member.repository;

import java.util.List;

import way.application.infrastructure.member.entity.MemberEntity;

public interface MemberRepository {
	MemberEntity validateMemberSeq(Long memberSeq);
	List<MemberEntity> validateMemberSeqs(List<Long> memberSeqs);
}

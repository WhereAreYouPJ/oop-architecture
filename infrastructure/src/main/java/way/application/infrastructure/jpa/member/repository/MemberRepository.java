package way.application.infrastructure.jpa.member.repository;

import java.util.List;

import way.application.infrastructure.jpa.member.entity.MemberEntity;

public interface MemberRepository {
	// Member Seq Validation 메서드
	MemberEntity findByMemberSeq(Long memberSeq);

	// Member Seqs Validation 메서드
	List<MemberEntity> findByMemberSeqs(List<Long> memberSeqs);

	MemberEntity saveMember(MemberEntity memberEntity);

	void isDuplicatedEmail(String email);

	MemberEntity validateEmail(String email);

	void saveRefreshToken(String refreshToken, String email);

    void saveAuthKey(String email, String authKey);

	String getCode(String email);

	void deleteCode(String email);

	void deleteJwt(String email);

    MemberEntity findByMemberCode(String memberCode);
}
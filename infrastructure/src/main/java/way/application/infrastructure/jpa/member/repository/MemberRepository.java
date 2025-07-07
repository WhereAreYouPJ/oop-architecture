package way.application.infrastructure.jpa.member.repository;

import java.util.List;

import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

public interface MemberRepository {
	MemberEntity findByMemberSeq(Long memberSeq);

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

    List<MemberEntity> findByFeedEntity(FeedEntity feedEntity,MemberEntity memberEntity);

	MemberEntity findByEmail(String email);

	void deleteByMemberSeq(MemberEntity memberEntity);

	List<MemberEntity> findByScheduleEntityAcceptTrue(ScheduleEntity scheduleEntity);

	MemberEntity findByKakaoId(String id);

	void isDuplicatedKakao(String code);

	MemberEntity findByAppleId(String appleId);

	void isDuplicatedApple(String code);
}

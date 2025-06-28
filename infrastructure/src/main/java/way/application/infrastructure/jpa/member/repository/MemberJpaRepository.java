package way.application.infrastructure.jpa.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.member.entity.MemberEntity;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByMemberCode(String memberCode);

    Optional<MemberEntity> findByKakaoPassword(String kakao);
}

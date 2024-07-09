package way.application.infrastructure.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.member.entity.MemberEntity;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

}

package way.application.infrastructure.bookMark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Repository
public interface BookMarkJpaRepository extends JpaRepository<BookMarkEntity, Long> {
	void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

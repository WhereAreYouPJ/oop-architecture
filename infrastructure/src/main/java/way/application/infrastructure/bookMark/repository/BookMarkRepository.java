package way.application.infrastructure.bookMark.repository;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

public interface BookMarkRepository {
	void saveBookMarkEntity(BookMarkEntity bookMarkEntity);

	void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}

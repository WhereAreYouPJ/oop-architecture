package way.application.infrastructure.bookMark.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Component
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository {
	private final BookMarkJpaRepository bookMarkJpaRepository;

	@Override
	public void saveBookMarkEntity(BookMarkEntity bookMarkEntity) {
		bookMarkJpaRepository.save(bookMarkEntity);
	}

	@Override
	public void deleteAllByScheduleEntityAndMemberEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity) {
		bookMarkJpaRepository.deleteAllByScheduleEntityAndMemberEntity(scheduleEntity, memberEntity);
	}
}

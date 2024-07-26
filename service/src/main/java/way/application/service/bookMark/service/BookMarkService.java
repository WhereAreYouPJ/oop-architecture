package way.application.service.bookMark.service;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.bookMark.mapper.BookMarkMapper;

@Service
@RequiredArgsConstructor
public class BookMarkService {
	private final ScheduleRepository scheduleRepository;
	private final MemberRepository memberRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final BookMarkRepository bookMarkRepository;

	private final BookMarkMapper bookMarkMapper;

	@Transactional
	public void addBookMark(AddBookMarkRequestDto addBookMarkRequestDto) {
		// 유효성 검사 (Repo 단)
		MemberEntity bookMarkMemberEntity = memberRepository.findByMemberSeq(addBookMarkRequestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(addBookMarkRequestDto.scheduleSeq());
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
			addBookMarkRequestDto.scheduleSeq(),
			addBookMarkRequestDto.memberSeq()
		);
		ScheduleEntity bookMarkScheduleEntity = scheduleMemberEntity.getSchedule();

		// 책갈피 저장
		bookMarkRepository.saveBookMarkEntity(
			bookMarkMapper.toBookMarkEntity(bookMarkScheduleEntity, bookMarkMemberEntity)
		);
	}

	@Transactional
	public void deleteBookMark(DeleteBookMarkRequestDto deleteBookMarkRequestDto) {
		// 예외처리 (Repo 단)
		MemberEntity bookMarkMemberEntity = memberRepository.findByMemberSeq(deleteBookMarkRequestDto.memberSeq());
		scheduleRepository.findByScheduleSeq(deleteBookMarkRequestDto.bookMarkScheduleSeq());
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
			deleteBookMarkRequestDto.bookMarkScheduleSeq(),
			deleteBookMarkRequestDto.memberSeq()
		);
		ScheduleEntity bookMarkScheduleEntity = scheduleMemberEntity.getSchedule();

		// 책갈피 삭제
		bookMarkRepository.deleteAllByScheduleEntityAndMemberEntity(bookMarkScheduleEntity, bookMarkMemberEntity);
	}
}

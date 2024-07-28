package way.application.service.bookMark.service;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;
import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.repository.FeedRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.service.bookMark.mapper.BookMarkMapper;

@Service
@RequiredArgsConstructor
public class BookMarkService {
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;
	private final BookMarkRepository bookMarkRepository;

	private final BookMarkMapper bookMarkMapper;

	@Transactional
	public AddBookMarkResponseDto addBookMarkFeed(AddBookMarkRequestDto addBookMarkResponseDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Feed 작성자 확인
		 3. Book Mark Feed 존재 여부 확인 (존재 시 Exception)
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(addBookMarkResponseDto.memberSeq());
		feedRepository.findByFeedSeq(addBookMarkResponseDto.bookMarkFeedSeq());
		FeedEntity feedEntity = feedRepository.findByCreatorMemberAndFeedSeq(
			memberEntity,
			addBookMarkResponseDto.bookMarkFeedSeq()
		);
		bookMarkRepository.checkBookMarkFeedEntityByFeedEntityAndMemberEntity(
			feedEntity,
			memberEntity
		);

		// Hide Feed 저장
		BookMarkEntity bookMarkEntity = bookMarkRepository.saveBookMarkEntity(
			bookMarkMapper.toBookMarkEntity(feedEntity, memberEntity)
		);

		return new AddBookMarkResponseDto(bookMarkEntity.getBookMarkSeq());
	}
}

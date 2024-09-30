package way.application.service.bookMark.service;

import static way.application.service.bookMark.dto.request.BookMarkRequestDto.*;
import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.service.bookMark.mapper.BookMarkMapper;

@Service
@RequiredArgsConstructor
public class BookMarkService {
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;
	private final BookMarkRepository bookMarkRepository;
	private final FeedImageRepository feedImageRepository;

	private final BookMarkMapper bookMarkMapper;

	@Transactional
	public AddBookMarkResponseDto addBookMarkFeed(AddBookMarkRequestDto requestDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Book Mark Feed 존재 여부 확인 (존재 시 Exception)
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		FeedEntity feedEntity = feedRepository.findByFeedSeq(requestDto.feedSeq());
		bookMarkRepository.checkBookMarkFeedEntityByFeedEntityAndMemberEntity(feedEntity, memberEntity);

		// Hide Feed 저장
		BookMarkEntity bookMarkEntity
			= bookMarkRepository.saveBookMarkEntity(bookMarkMapper.toBookMarkEntity(feedEntity, memberEntity));

		return bookMarkMapper.toAddBookMarkResponseDto(bookMarkEntity);
	}

	@Transactional
	public void deleteBookMarkFeed(DeleteBookMarkRequestDto requestDto) {
		/*
		 1. Member 확인
		 2. Book Mark 확인
		*/
		memberRepository.findByMemberSeq(requestDto.memberSeq());
		BookMarkEntity bookMarkEntity = bookMarkRepository.findByBookMarkSeq(requestDto.bookMarkFeedSeq());

		// Book Mark Feed 삭제
		bookMarkRepository.deleteBookMarkEntity(bookMarkEntity);
	}

	@Transactional(readOnly = true)
	public Page<GetBookMarkResponseDto> getBookMark(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		Page<BookMarkEntity> bookMarkEntityPage
			= bookMarkRepository.findAllByMemberEntityOrderByScheduleStartTimeDesc(memberEntity, pageable);

		return bookMarkEntityPage.map(bookMarkEntity -> {
			List<FeedImageEntity> feedImageEntities
				= feedImageRepository.findAllByFeedEntity(bookMarkEntity.getFeedEntity());

			return bookMarkMapper.toGetBookMarkResponseDto(bookMarkEntity, feedImageEntities);
		});
	}
}

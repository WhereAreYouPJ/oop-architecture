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
import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.repository.FeedRepository;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.service.bookMark.mapper.BookMarkMapper;
import way.application.service.hideFeed.dto.response.HideFeedResponseDto;

@Service
@RequiredArgsConstructor
public class BookMarkService {
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;
	private final BookMarkRepository bookMarkRepository;
	private final FeedImageRepository feedImageRepository;

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

	@Transactional
	public void deleteBookMarkFeed(DeleteBookMarkRequestDto deleteBookMarkRequestDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Book Mark Feed 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(deleteBookMarkRequestDto.memberSeq());
		FeedEntity feedEntity = feedRepository.findByFeedSeq(deleteBookMarkRequestDto.bookMarkFeedSeq());
		BookMarkEntity bookMarkEntity = bookMarkRepository.findByFeedEntityAndMemberEntity(
			feedEntity,
			memberEntity
		);

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
			FeedEntity feedEntity = bookMarkEntity.getFeedEntity();
			ScheduleEntity scheduleEntity = feedEntity.getSchedule();

			// Feed 이미지 가져오기
			List<String> feedImageUrl = feedImageRepository.findAllByFeedEntity(feedEntity)
				.stream()
				.map(FeedImageEntity::getFeedImageURL)
				.collect(Collectors.toList());

			return new GetBookMarkResponseDto(
				bookMarkEntity.getMemberEntity().getProfileImage(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation(),
				feedEntity.getTitle(),
				feedImageUrl,
				feedEntity.getContent(),
				true
			);
		});
	}
}

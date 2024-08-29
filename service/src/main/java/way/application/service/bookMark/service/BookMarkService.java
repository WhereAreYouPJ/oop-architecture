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
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
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
		 3. Book Mark Feed 존재 여부 확인 (존재 시 Exception)
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(addBookMarkResponseDto.memberSeq());
		FeedEntity feedEntity = feedRepository.findByFeedSeq(addBookMarkResponseDto.feedSeq());
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
		 2. Book Mark 확인
		*/
		memberRepository.findByMemberSeq(deleteBookMarkRequestDto.memberSeq());
		BookMarkEntity bookMarkEntity
			= bookMarkRepository.findByBookMarkSeq(deleteBookMarkRequestDto.bookMarkFeedSeq());

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
			MemberEntity creatorMemberEntity = feedEntity.getCreatorMember();

			// Feed 이미지 가져오기
			List<BookMarkImageInfo> bookMarkImageInfos = feedImageRepository.findAllByFeedEntity(feedEntity).stream()
				.map(feedImageEntity -> new BookMarkImageInfo(
					feedImageEntity.getFeedImageSeq(),
					feedImageEntity.getFeedImageURL(),
					feedImageEntity.getFeedImageOrder()
				))
				.toList();

			return new GetBookMarkResponseDto(
				creatorMemberEntity.getMemberSeq(),
				bookMarkEntity.getMemberEntity().getProfileImage(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation(),
				feedEntity.getTitle(),
				bookMarkImageInfos,
				feedEntity.getContent(),
				true
			);
		});
	}
}

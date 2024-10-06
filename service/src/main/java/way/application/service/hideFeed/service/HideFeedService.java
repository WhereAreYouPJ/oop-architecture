package way.application.service.hideFeed.service;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;
import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.jpa.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.service.hideFeed.mapper.HideFeedEntityMapper;

@Service
@RequiredArgsConstructor
public class HideFeedService {
	private final HideFeedRepository hideFeedRepository;
	private final BookMarkRepository bookMarkRepository;
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;
	private final FeedImageRepository feedImageRepository;

	private final HideFeedEntityMapper hideFeedMapper;

	@Transactional
	public AddHideFeedResponseDto addHideFeed(AddHideFeedRequestDto requestDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Hide Feed 존재 여부 확인 (존재 시 Exception)
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		FeedEntity feedEntity = feedRepository.findByFeedSeq(requestDto.feedSeq());
		hideFeedRepository.verifyHideFeedNotExists(feedEntity, memberEntity);

		// Hide Feed 저장
		HideFeedEntity hideFeedEntity = hideFeedMapper.toHideFeedEntity(feedEntity, memberEntity);
		HideFeedEntity savedHideFeedEntity = hideFeedRepository.saveHideFeedEntity(hideFeedEntity);

		return hideFeedMapper.toAddHideFeedResponseDto(savedHideFeedEntity);
	}

	@Transactional
	public void deleteHideFeed(DeleteHideFeedRequestDto requestDto) {
		/*
		 1. Member 확인
		 2. Hide Feed 확인
		*/
		memberRepository.findByMemberSeq(requestDto.memberSeq());
		HideFeedEntity hideFeedEntity = hideFeedRepository.findByHideFeedSeq(requestDto.hideFeedSeq());

		// Hide Feed 삭제
		hideFeedRepository.deleteHideFeedEntity(hideFeedEntity);
	}

	@Transactional(readOnly = true)
	public Page<GetHideFeedResponseDto> getHideFeed(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// HideFeedEntity 가져오기
		Page<HideFeedEntity> hideFeedEntityPage
			= hideFeedRepository.findAllByMemberEntityOrderByScheduleStartTimeDesc(memberEntity, pageable);

		return hideFeedEntityPage.map(hideFeedEntity -> {
			FeedEntity feedEntity = hideFeedEntity.getFeedEntity();

			// Feed 이미지 가져오기 및 ImageInfo로 변환
			List<hideFeedImageInfo> hideFeedImageInfos = feedImageRepository.findAllByFeedEntity(feedEntity).stream()
				.map(hideFeedMapper::toHideFeedImageInfo)
				.toList();

			Boolean bookMark
				= bookMarkRepository.existsByFeedEntityAndMemberEntity(feedEntity, hideFeedEntity.getMemberEntity());

			return hideFeedMapper.toGetHideFeedResponseDto(hideFeedEntity, hideFeedImageInfos, bookMark);
		});
	}
}

package way.application.service.hideFeed.service;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;
import static way.application.service.hideFeed.dto.response.HideFeedResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.repository.FeedRepository;
import way.application.infrastructure.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.service.hideFeed.mapper.HideFeedMapper;

@Service
@RequiredArgsConstructor
public class HideFeedService {
	private final HideFeedRepository hideFeedRepository;
	private final MemberRepository memberRepository;
	private final FeedRepository feedRepository;
	private final FeedImageRepository feedImageRepository;

	private final HideFeedMapper hideFeedMapper;

	@Transactional
	public AddHideFeedResponseDto addHideFeed(AddHideFeedRequestDto addHideFeedRequestDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Feed 작성자 확인
		 3. Hide Feed 존재 여부 확인 (존재 시 Exception)
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(addHideFeedRequestDto.memberSeq());
		feedRepository.findByFeedSeq(addHideFeedRequestDto.hideFeedSeq());
		FeedEntity feedEntity = feedRepository.findByCreatorMemberAndFeedSeq(
			memberEntity,
			addHideFeedRequestDto.hideFeedSeq()
		);
		hideFeedRepository.checkHideFeedEntityByFeedEntityAndMemberEntity(
			feedEntity,
			memberEntity
		);

		// Hide Feed 저장
		HideFeedEntity hideFeedEntity = hideFeedRepository.saveHideFeedEntity(
			hideFeedMapper.toHideFeedEntity(feedEntity, memberEntity)
		);

		return new AddHideFeedResponseDto(hideFeedEntity.getHideFeedSeq());
	}

	@Transactional
	public void deleteHideFeed(DeleteHideFeedRequestDto hideFeedRequestDto) {
		/*
		 1. Member 확인
		 2. Feed 확인
		 3. Hide Feed 작성자 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(hideFeedRequestDto.memberSeq());
		FeedEntity feedEntity = feedRepository.findByFeedSeq(hideFeedRequestDto.hideFeedSeq());
		HideFeedEntity hideFeedEntity = hideFeedRepository.findHideFeedEntityByFeedEntityAndMemberEntity(
			feedEntity,
			memberEntity
		);

		// Hide Feed 삭제
		hideFeedRepository.deleteHideFeedEntity(hideFeedEntity);
	}

	@Transactional(readOnly = true)
	public Page<GetHideFeedResponseDto> getHideFeed(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		// 2. HideFeedEntity 가져오기
		Page<HideFeedEntity> hideFeedEntityPage = hideFeedRepository.findAllByMemberEntity(memberEntity, pageable);

		// 3. HideFeedEntity를 GetHideFeedResponseDto로 변환
		return hideFeedEntityPage.map(hideFeedEntity -> {
			FeedEntity feedEntity = hideFeedEntity.getFeedEntity();
			ScheduleEntity scheduleEntity = feedEntity.getSchedule();

			// Feed 이미지 가져오기
			List<String> feedImageUrl = feedImageRepository.findAllByFeedEntity(feedEntity)
				.stream()
				.map(FeedImageEntity::getFeedImageURL)
				.collect(Collectors.toList());

			// TODO: 북마크 상태 확인 (구현에 따라 수정 필요)
			// Boolean bookMark = checkBookMarkStatus(feedEntity, hideFeedEntity.getMemberEntity());

			return new GetHideFeedResponseDto(
				hideFeedEntity.getMemberEntity().getProfileImage(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation(),
				feedEntity.getTitle(),
				feedImageUrl,
				feedEntity.getContent()
			);
		});
	}
}

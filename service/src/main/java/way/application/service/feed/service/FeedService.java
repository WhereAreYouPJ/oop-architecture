package way.application.service.feed.service;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.GetFeedResponseDto.*;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.feed.mapper.FeedEntityMapper;
import way.application.service.feedImage.mapper.FeedImageMapper;
import way.application.utils.s3.S3Utils;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final MemberRepository memberRepository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final FeedRepository feedRepository;
	private final FeedImageRepository feedImageRepository;
	private final BookMarkRepository bookMarkRepository;

	private final S3Utils s3Utils;

	private final FeedEntityMapper feedEntityMapper;
	private final FeedImageMapper feedImageMapper;

	@Transactional
	public SaveFeedResponseDto saveFeed(SaveFeedRequestDto requestDto) throws IOException {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schedule Member In (Accept = True) 유효성 검사
		 4. 이미 Feed 생성 여부 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(
			requestDto.scheduleSeq(),
			requestDto.memberSeq()
		);
		feedRepository.findByCreatorMemberAndSchedule(memberEntity, scheduleEntity);

		// Feed Entity 저장
		FeedEntity feedEntity
			= feedEntityMapper.toFeedEntity(scheduleEntity, memberEntity, requestDto.title(), requestDto.content());
		FeedEntity savedFeedEntity = feedRepository.saveFeedEntity(feedEntity);

		// Feed Image Entity 저장
		if (requestDto.feedImageInfos() != null) {
			for (feedImageInfo feedImageInfo : requestDto.feedImageInfos()) {
				String imageURL = s3Utils.uploadMultipartFile(feedImageInfo.images());

				// Feed Image Entity 생성
				feedImageRepository.saveFeedImageEntity(
					feedImageMapper.toFeedImageEntity(savedFeedEntity, imageURL, feedImageInfo.feedImageOrder())
				);
			}
		}

		return feedEntityMapper.toSaveFeedResponseDto(feedEntity);
	}

	@Transactional
	public ModifyFeedResponseDto modifyFeed(ModifyFeedRequestDto requestDto) throws IOException {
		/*
		 1. Member 유효성 검사
		 2. Feed 유효성 검사
		 3. Feed 작성자 검사
		*/
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		feedRepository.findByFeedSeq(requestDto.feedSeq());
		FeedEntity savedFeed = feedRepository.findByCreatorMemberAndFeedSeq(creatorMemberEntity, requestDto.feedSeq());

		// Feed, Feed Image
		feedRepository.deleteAllByFeedSeq(savedFeed.getFeedSeq());
		feedImageRepository.deleteAllByFeedEntity(savedFeed);

		SaveFeedResponseDto saveFeedResponseDto
			= saveFeed(requestDto.toSaveFeedRequestDto(savedFeed.getSchedule().getScheduleSeq()));

		return saveFeedResponseDto.toModifyFeedResponseDto();
	}

	@Transactional(readOnly = true)
	public Page<GetFeedResponseDto> getAllFeed(Long memberSeq, Pageable pageable) {
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		return scheduleRepository.getScheduleEntityFromScheduleMember(
			scheduleMemberRepository.findByMemberEntity(memberEntity, pageable)
		).map(scheduleEntity -> {
			ScheduleInfo scheduleInfo = feedEntityMapper.toScheduleInfo(scheduleEntity);

			FeedEntity feedEntity = feedRepository.findByScheduleExcludingHiddenRand(scheduleEntity, memberEntity);
			boolean bookMarkInfo = bookMarkRepository.existsByFeedEntityAndMemberEntity(feedEntity, memberEntity);
			List<FeedImageEntity> feedImageEntities = feedImageRepository.findAllByFeedEntity(feedEntity);

			ScheduleFeedInfo scheduleFeedInfo = feedEntityMapper.toScheduleFeedInfo(
				feedEntity.getCreatorMember(),
				feedEntity,
				feedImageEntities,
				bookMarkInfo
			);

			return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, List.of(scheduleFeedInfo));
		});
	}

	@Transactional(readOnly = true)
	public GetFeedResponseDto getFeed(Long memberSeq, Long scheduleSeq) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Member Schedule 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(scheduleSeq);
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(scheduleSeq, memberSeq);

		// Feed 조회 및 변환
		List<ScheduleFeedInfo> scheduleFeedInfos = feedRepository.findByScheduleEntity(scheduleEntity).stream()
			.map(feedEntity -> {
				boolean bookMarkInfo = bookMarkRepository.existsByFeedEntityAndMemberEntity(feedEntity, memberEntity);
				List<FeedImageEntity> feedImageEntities = feedImageRepository.findAllByFeedEntity(feedEntity);

				return feedEntityMapper.toScheduleFeedInfo(
					feedEntity.getCreatorMember(),
					feedEntity,
					feedImageEntities,
					bookMarkInfo
				);
			}).toList();

		// Schedule Info 생성
		ScheduleInfo scheduleInfo = feedEntityMapper.toScheduleInfo(scheduleEntity);

		return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, scheduleFeedInfos);
	}
}

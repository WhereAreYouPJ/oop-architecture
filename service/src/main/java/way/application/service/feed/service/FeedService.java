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
		// Member 유효성 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// ScheduleMemberEntity를 통해 Schedule을 조회하고 각 Schedule에 대해 Feed 정보를 조회
		return scheduleRepository.getScheduleEntityFromScheduleMember(
			scheduleMemberRepository.findByMemberEntity(memberEntity, pageable)
		).map(scheduleEntity -> {
			// Schedule 정보 생성
			ScheduleInfo scheduleInfo = new ScheduleInfo(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation()
			);

			// 해당 Schedule의 Feed 조회 및 정보 생성
			FeedEntity feedEntity = feedRepository.findByScheduleExcludingHiddenRand(scheduleEntity, memberEntity);
			FeedInfo feedInfo = new FeedInfo(
				feedEntity.getFeedSeq(),
				feedEntity.getTitle(),
				feedEntity.getContent()
			);

			// Feed 작성자 정보
			MemberEntity creatorMember = feedEntity.getCreatorMember();
			MemberInfo memberInfo = new MemberInfo(
				creatorMember.getMemberSeq(),
				creatorMember.getUserName(),
				creatorMember.getProfileImage()
			);

			// Book Mark 여부 확인
			boolean bookMarkInfo = bookMarkRepository.isFeedBookMarkedByMember(feedEntity, memberEntity);

			// Feed 이미지 정보 생성
			List<FeedImageInfo> feedImageInfos = feedImageRepository.findAllByFeedEntity(feedEntity).stream()
				.map(feedImageEntity -> new FeedImageInfo(
					feedImageEntity.getFeedImageSeq(),
					feedImageEntity.getFeedImageURL(),
					feedImageEntity.getFeedImageOrder()
				)).toList();

			// ScheduleFeedInfo 생성 및 응답 객체 생성
			ScheduleFeedInfo scheduleFeedInfo = new ScheduleFeedInfo(
				memberInfo,
				feedInfo,
				feedImageInfos,
				bookMarkInfo
			);

			return new GetFeedResponseDto(scheduleInfo, List.of(scheduleFeedInfo));
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
				// Member Info 생성
				MemberInfo memberInfo = new MemberInfo(
					feedEntity.getCreatorMember().getMemberSeq(),
					feedEntity.getCreatorMember().getUserName(),
					feedEntity.getCreatorMember().getProfileImage()
				);

				// Feed Info 생성
				FeedInfo feedInfo = new FeedInfo(
					feedEntity.getFeedSeq(),
					feedEntity.getTitle(),
					feedEntity.getContent()
				);

				// Feed 이미지 정보 생성
				List<FeedImageInfo> feedImageInfos = feedImageRepository.findAllByFeedEntity(feedEntity).stream()
					.map(feedImageEntity -> new FeedImageInfo(
						feedImageEntity.getFeedImageSeq(),
						feedImageEntity.getFeedImageURL(),
						feedImageEntity.getFeedImageOrder()
					)).toList();

				// Book Mark 여부 확인
				boolean bookMarkInfo = bookMarkRepository.isFeedBookMarkedByMember(feedEntity, memberEntity);

				// ScheduleFeedInfo 생성
				return new ScheduleFeedInfo(memberInfo, feedInfo, feedImageInfos, bookMarkInfo);
			}).toList();

		// Schedule Info 생성
		ScheduleInfo scheduleInfo = new ScheduleInfo(
			scheduleEntity.getScheduleSeq(),
			scheduleEntity.getStartTime(),
			scheduleEntity.getLocation()
		);

		return new GetFeedResponseDto(scheduleInfo, scheduleFeedInfos);
	}
}

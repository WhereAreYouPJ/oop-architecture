package way.application.service.feed.service;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.GetAllFeedResponseDto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import way.application.service.feed.mapper.FeedMapper;
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

	private final FeedMapper feedMapper;
	private final FeedImageMapper feedImageMapper;

	@Transactional
	public SaveFeedResponseDto saveFeed(SaveFeedRequestDto saveFeedRequestDto) throws IOException {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		 3. Schedule Member In (Accept = True) 유효성 검사
		 4. 이미 Feed 생성 여부 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(saveFeedRequestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(saveFeedRequestDto.scheduleSeq());
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(
			saveFeedRequestDto.scheduleSeq(),
			saveFeedRequestDto.memberSeq()
		);
		feedRepository.findByCreatorMemberAndSchedule(memberEntity, scheduleEntity);

		// Feed Entity 생성
		FeedEntity feedEntity = feedMapper.toFeedEntity(
			scheduleEntity,
			memberEntity,
			saveFeedRequestDto.title(),
			saveFeedRequestDto.content()
		);

		// Feed Entity 저장
		FeedEntity savedFeedEntity = feedRepository.saveFeedEntity(feedEntity);

		// Feed Image Entity 저장
		if (saveFeedRequestDto.feedImageInfos() != null) {
			for (feedImageInfo feedImageInfo : saveFeedRequestDto.feedImageInfos()) {
				String imageURL = s3Utils.uploadMultipartFile(feedImageInfo.images());

				// Feed Image Entity 생성
				feedImageRepository.saveFeedImageEntity(
					feedImageMapper.toFeedImageEntity(savedFeedEntity, imageURL, feedImageInfo.feedImageOrder())
				);
			}
		}

		return new SaveFeedResponseDto(savedFeedEntity.getFeedSeq());
	}

	@Transactional
	public ModifyFeedResponseDto modifyFeed(ModifyFeedRequestDto modifyFeedRequestDto) throws IOException {
		/*
		 1. Member 유효성 검사
		 2. Feed 유효성 검사
		 3. Feed 작성자 검사
		*/
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(modifyFeedRequestDto.memberSeq());
		feedRepository.findByFeedSeq(modifyFeedRequestDto.feedSeq());
		FeedEntity savedFeed = feedRepository.findByCreatorMemberAndFeedSeq(
			creatorMemberEntity,
			modifyFeedRequestDto.feedSeq()
		);

		// Feed, Feed Image
		feedRepository.deleteAllByFeedSeq(savedFeed.getFeedSeq());
		feedImageRepository.deleteAllByFeedEntity(savedFeed);

		SaveFeedResponseDto saveFeedResponseDto = saveFeed(
			modifyFeedRequestDto.toSaveFeedRequestDto(savedFeed.getSchedule().getScheduleSeq())
		);

		return new ModifyFeedResponseDto(saveFeedResponseDto.feedSeq());
	}

	@Transactional(readOnly = true)
	public Page<GetAllFeedResponseDto> getAllFeed(Long memberSeq, Pageable pageable) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// ScheduleMemberEntity 조회 -> Schedule 추출
		Page<ScheduleEntity> scheduleEntityPage = scheduleRepository.getScheduleEntityFromScheduleMember(
			scheduleMemberRepository.findByMemberEntity(memberEntity, pageable)
		);

		// Schedule 별 Feed 조회 및 응답 생성
		List<GetAllFeedResponseDto> response = new ArrayList<>();

		for (ScheduleEntity scheduleEntity : scheduleEntityPage) {
			// Schedule Feed Info List 생성
			List<ScheduleFeedInfo> scheduleFeedInfos = new ArrayList<>();

			// 해당 Schedule Info 생성
			ScheduleInfo scheduleInfo = new ScheduleInfo(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation()
			);

			// Schedule 에 해당하는 Feed 반환 (작성자 중심 -> 없으면 무작위)
			FeedEntity feedEntity
				= feedRepository.findByScheduleExcludingHiddenRand(scheduleEntity, memberEntity);
			FeedInfo feedInfo = new FeedInfo(
				feedEntity.getFeedSeq(),
				feedEntity.getTitle(),
				feedEntity.getContent()
			);

			// Feed 작성자 반환
			MemberEntity creatorMember = feedEntity.getCreatorMember();
			MemberInfo memberInfo = new MemberInfo(
				creatorMember.getMemberSeq(),
				creatorMember.getUserName(),
				creatorMember.getProfileImage()
			);

			// Book Mark 여부 확인
			boolean bookMarkInfo = bookMarkRepository.isFeedBookMarkedByMember(feedEntity, memberEntity);

			// Feed Image 반환
			List<FeedImageEntity> feedImageEntities = feedImageRepository.findAllByFeedEntity(feedEntity);
			List<FeedImageInfo> feedImageInfos = feedImageEntities.stream()
				.map(feedImageEntity -> new FeedImageInfo(
					feedImageEntity.getFeedImageSeq(),
					feedImageEntity.getFeedImageURL(),
					feedImageEntity.getFeedImageOrder()
				))
				.toList();

			ScheduleFeedInfo scheduleFeedInfo = new ScheduleFeedInfo(
				memberInfo,
				feedInfo,
				feedImageInfos,
				bookMarkInfo
			);

			scheduleFeedInfos.add(scheduleFeedInfo);
			GetAllFeedResponseDto getAllFeedResponseDto = new GetAllFeedResponseDto(
				scheduleInfo,
				scheduleFeedInfos
			);

			response.add(getAllFeedResponseDto);
		}

		return new PageImpl<>(response, pageable, response.size());
	}
}

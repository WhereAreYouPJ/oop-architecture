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
import org.springframework.web.multipart.MultipartFile;

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
		 1. Member
		 2. Schedule
		 3. Schedule 수락 여부
		 4. Feed 존재 여부
		*/
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(saveFeedRequestDto.creatorSeq());
		ScheduleEntity savedSchedule = scheduleRepository.findByScheduleSeq(saveFeedRequestDto.scheduleSeq());
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(
			saveFeedRequestDto.scheduleSeq(),
			saveFeedRequestDto.creatorSeq()
		);
		feedRepository.findByCreatorMemberAndSchedule(creatorMemberEntity, savedSchedule);

		// Feed Entity 생성
		FeedEntity feedEntity = feedMapper.toFeedEntity(
			savedSchedule,
			creatorMemberEntity,
			saveFeedRequestDto.title(),
			saveFeedRequestDto.content()
		);

		// Feed Entity 저장
		FeedEntity savedFeedEntity = feedRepository.saveFeedEntity(feedEntity);

		// Feed Image Entity 저장
		if (saveFeedRequestDto.images() != null) {
			for (MultipartFile image : saveFeedRequestDto.images()) {
				String imageURL = s3Utils.uploadMultipartFile(image);

				// Feed Image Entity 생성
				feedImageRepository.saveFeedImageEntity(
					feedImageMapper.toFeedImageEntity(savedFeedEntity, imageURL)
				);
			}
		}

		return new SaveFeedResponseDto(savedFeedEntity.getFeedSeq());
	}

	@Transactional
	public ModifyFeedResponseDto modifyFeed(ModifyFeedRequestDto modifyFeedRequestDto) throws IOException {
		// 유효성 처리 (Repo 단)
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(modifyFeedRequestDto.creatorSeq());
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
		// 1. Member 유효성 검사
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// ScheduleMemberEntity 조회 -> Schedule 추출
		Page<ScheduleEntity> scheduleEntityPage = scheduleRepository.getScheduleEntityFromScheduleMember(
			scheduleMemberRepository.findByMemberEntity(memberEntity, pageable)
		);

		// Schedule 별 Feed 조회 및 응답 생성
		List<GetAllFeedResponseDto> response = new ArrayList<>();

		scheduleEntityPage.forEach(scheduleEntity -> {
			ScheduleInfo scheduleInfo = new ScheduleInfo(
				scheduleEntity.getScheduleSeq(),
				scheduleEntity.getStartTime(),
				scheduleEntity.getLocation()
			);

			feedRepository.findByScheduleExcludingHidden(scheduleEntity, memberEntity, pageable)
				.forEach(feedEntity -> {
					FeedInfo feedInfo = new FeedInfo(
						feedEntity.getFeedSeq(),
						feedEntity.getTitle(),
						feedEntity.getContent()
					);

					FeedImageInfo feedImageInfo = new FeedImageInfo(
						feedImageRepository.findFeedImageURLsByFeedEntity(feedEntity)
					);

					BookMarkInfo bookMarkInfo = new BookMarkInfo(
						bookMarkRepository.isFeedBookMarkedByMember(feedEntity, memberEntity)
					);

					MemberInfo memberInfo = new MemberInfo(
						feedEntity.getCreatorMember().getMemberSeq(),
						feedEntity.getCreatorMember().getUserName(),
						feedEntity.getCreatorMember().getProfileImage()
					);

					List<ScheduleFeedInfo> scheduleFeedInfos = new ArrayList<>();
					scheduleFeedInfos.add(
						new ScheduleFeedInfo(memberInfo, scheduleInfo, feedInfo, feedImageInfo, bookMarkInfo)
					);

					response.add(new GetAllFeedResponseDto(scheduleFeedInfos));
				});
		});

		return new PageImpl<>(response, pageable, response.size());
	}
}

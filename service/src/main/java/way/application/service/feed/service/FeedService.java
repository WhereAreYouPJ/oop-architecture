package way.application.service.feed.service;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.repository.FeedRepository;
import way.application.infrastructure.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
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
		scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
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
}

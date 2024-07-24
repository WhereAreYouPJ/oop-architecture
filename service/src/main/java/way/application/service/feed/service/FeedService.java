package way.application.service.feed.service;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import way.application.domain.feed.FeedDomain;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.feed.repository.FeedRepository;
import way.application.infrastructure.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.feedMember.repository.FeedMemberRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.schedule.repository.ScheduleRepository;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.feed.mapper.FeedMapper;
import way.application.service.feedImage.mapper.FeedImageMapper;
import way.application.service.feedMember.mapper.FeedMemberMapper;
import way.application.utils.s3.S3Utils;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final MemberRepository memberRepository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;
	private final FeedRepository feedRepository;
	private final FeedMemberRepository feedMemberRepository;
	private final FeedImageRepository feedImageRepository;

	private final S3Utils s3Utils;

	private final FeedMapper feedMapper;
	private final FeedMemberMapper feedMemberMapper;
	private final FeedImageMapper feedImageMapper;

	private final FeedDomain feedDomain;

	@Transactional
	public SaveFeedResponseDto saveFeed(SaveFeedRequestDto saveFeedRequestDto) throws IOException {
		// 유효성 처리 (Repo 단)
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(saveFeedRequestDto.creatorSeq());
		ScheduleEntity savedSchedule = scheduleRepository.findByScheduleSeq(saveFeedRequestDto.scheduleSeq());

		// Schedule 수락 여부 확인
		scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
			saveFeedRequestDto.scheduleSeq(),
			saveFeedRequestDto.creatorSeq()
		);

		// 해당 Schedule 의 Member 추출 -> Schedule accept -> true 인 경우
		List<ScheduleMemberEntity> scheduleMemberEntities
			= scheduleMemberRepository.findAcceptedScheduleMemberByScheduleEntity(savedSchedule);

		// Feed Entity 생성
		FeedEntity feedEntity = feedMapper.toFeedEntity(
			savedSchedule,
			creatorMemberEntity,
			saveFeedRequestDto.title(),
			saveFeedRequestDto.content()
		);

		// Feed Entity 저장
		FeedEntity savedFeedEntity = feedRepository.saveFeedEntity(feedEntity);

		// Feed Member Entity 저장
		for (ScheduleMemberEntity scheduleMemberEntity : scheduleMemberEntities) {
			MemberEntity invitedMember = scheduleMemberEntity.getInvitedMember();

			feedMemberRepository.saveFeedMemberEntity(
				feedMemberMapper.toFeedMemberEntity(savedFeedEntity, invitedMember)
			);
		}

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

		// Feed, Feed Image, Feed Member 삭제
		feedRepository.deleteAllByFeedSeq(savedFeed.getFeedSeq());
		feedImageRepository.deleteAllByFeedEntity(savedFeed);
		feedMemberRepository.deleteAllByFeedEntity(savedFeed);

		SaveFeedResponseDto saveFeedResponseDto = saveFeed(
			modifyFeedRequestDto.toSaveFeedRequestDto(savedFeed.getSchedule().getScheduleSeq())
		);

		return new ModifyFeedResponseDto(saveFeedResponseDto.feedSeq());
	}

	@Transactional
	public void hideFeed(HideFeedRequestDto hideFeedRequestDto) {
		// 유효성 처리 (Repo 단)
		MemberEntity creatorMemberEntity = memberRepository.findByMemberSeq(hideFeedRequestDto.creatorSeq());
		feedRepository.findByFeedSeq(hideFeedRequestDto.feedSeq());
		FeedEntity savedFeed = feedRepository.findByCreatorMemberAndFeedSeq(
			creatorMemberEntity,
			hideFeedRequestDto.feedSeq()
		);

		// Domain 처리
		FeedEntity toggledFeedEntity = feedDomain.toggleHide(savedFeed);

		// 저장
		feedRepository.saveFeedEntity(toggledFeedEntity);
	}
}

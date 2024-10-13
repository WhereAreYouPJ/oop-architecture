package way.application.service.feed.service;

import static way.application.service.feed.dto.request.FeedRequestDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.GetFeedResponseDto.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import way.application.domain.schedule.ScheduleDomain;
import way.application.domain.scheduleMember.ScheduleMemberDomain;
import way.application.infrastructure.jpa.bookMark.repository.BookMarkRepository;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feed.repository.FeedRepository;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.feedImage.repository.FeedImageRepository;
import way.application.infrastructure.jpa.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.infrastructure.jpa.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.jpa.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.feed.mapper.FeedEntityMapper;
import way.application.service.feedImage.mapper.FeedImageMapper;
import way.application.service.hideFeed.dto.response.HideFeedResponseDto;
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
	private final HideFeedRepository hideFeedRepository;

	private final S3Utils s3Utils;

	private final FeedEntityMapper feedEntityMapper;
	private final FeedImageMapper feedImageMapper;

	private final ScheduleDomain scheduleDomain;

	@Transactional
	public SaveFeedResponseDto saveFeed(SaveFeedRequestDto requestDto, List<MultipartFile> images) throws IOException {
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
		feedRepository.saveFeedEntity(feedEntity);

		if (images != null) {
			for (int i = 0; i < images.size(); i++) {
				MultipartFile multipartFile = images.get(i);
				String feedImageURL = s3Utils.uploadMultipartFile(multipartFile);
				Integer feedImageOrder = requestDto.feedImageOrders().get(i);

				FeedImageEntity feedImageEntity = feedImageMapper.toFeedImageEntity(feedEntity, feedImageURL,
					feedImageOrder);
				feedImageRepository.saveFeedImageEntity(feedImageEntity);
			}
		}

		return feedEntityMapper.toSaveFeedResponseDto(feedEntity);
	}

	@Transactional
	public ModifyFeedResponseDto modifyFeed(
		ModifyFeedRequestDto requestDto, List<MultipartFile> images
	) throws IOException {
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
			= saveFeed(requestDto.toSaveFeedRequestDto(savedFeed.getSchedule().getScheduleSeq()), images);

		return saveFeedResponseDto.toModifyFeedResponseDto();
	}

	@Transactional(readOnly = true)
	public Page<GetFeedResponseDto> getAllFeed(Long memberSeq, Pageable pageable) {
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		Page<ScheduleMemberEntity> scheduleMemberEntityPage
			= scheduleMemberRepository.findByMemberEntity(memberEntity, pageable);

		return scheduleDomain.getScheduleEntityFromScheduleMember(scheduleMemberEntityPage)
			.map(scheduleEntity -> {
				// userName 생성
				List<ScheduleMemberEntity> scheduleMemberEntityList
					= scheduleMemberRepository.findAllAcceptedScheduleMembersFriendsInSchedule(scheduleEntity, memberEntity);

				List<ScheduleFriendInfo> scheduleFriendInfo =
						scheduleMemberEntityList.stream()
								.map(scheduleMemberEntity -> new ScheduleFriendInfo(
										scheduleMemberEntity.getInvitedMember().getMemberSeq(),
										scheduleMemberEntity.getInvitedMember().getUserName(),
										scheduleMemberEntity.getInvitedMember().getProfileImage()
								))
								.toList();

				FeedEntity feedEntity = feedRepository.findByScheduleExcludingHiddenRand(scheduleEntity, memberEntity);

				return Optional.ofNullable(feedEntity)
					.map(fe -> {
						ScheduleInfo scheduleInfo = feedEntityMapper.toScheduleInfo(scheduleEntity);
						boolean bookMarkInfo = bookMarkRepository.existsByFeedEntityAndMemberEntity(fe, memberEntity);
						List<FeedImageEntity> feedImageEntities = feedImageRepository.findAllByFeedEntity(fe);

						ScheduleFeedInfo scheduleFeedInfo = feedEntityMapper.toScheduleFeedInfo(
							fe.getCreatorMember(),
							fe,
							feedImageEntities,
							bookMarkInfo
						);

						return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, List.of(scheduleFeedInfo), scheduleFriendInfo);
					})
					.orElse(null);
			})
			.filter(Objects::nonNull)
			.stream().collect(Collectors.collectingAndThen(Collectors.toList(), list ->
				new PageImpl<>(list, pageable, list.size())));
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
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(scheduleEntity.getScheduleSeq(), memberSeq);

		// USER NAME 추출
		List<ScheduleMemberEntity> scheduleMemberEntityList
			= scheduleMemberRepository.findAllAcceptedScheduleMembersFriendsInSchedule(scheduleEntity,memberEntity);

		List<ScheduleFriendInfo> scheduleFriendInfo =
				scheduleMemberEntityList.stream()
						.map(scheduleMemberEntity -> new ScheduleFriendInfo(
								scheduleMemberEntity.getInvitedMember().getMemberSeq(),
								scheduleMemberEntity.getInvitedMember().getUserName(),
								scheduleMemberEntity.getInvitedMember().getProfileImage()
						))
						.toList();

		// Feed 조회 및 변환
		List<ScheduleFeedInfo> scheduleFeedInfos = feedRepository.findByScheduleEntity(scheduleEntity).stream()
			.map(fe -> {
				boolean bookMarkInfo = bookMarkRepository.existsByFeedEntityAndMemberEntity(fe, memberEntity);
				List<FeedImageEntity> feedImageEntities = feedImageRepository.findAllByFeedEntity(fe);

				return feedEntityMapper.toScheduleFeedInfo(
					fe.getCreatorMember(),
					fe,
					feedImageEntities,
					bookMarkInfo
				);
			}).toList();

		// Schedule Info 생성
		ScheduleInfo scheduleInfo = feedEntityMapper.toScheduleInfo(scheduleEntity);

		return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, scheduleFeedInfos, scheduleFriendInfo);
	}

	@Transactional
	public void deleteFeed(DeleteFeedRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Feed 유효성 검사
		 3. 작성자 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		feedRepository.findByFeedSeq(requestDto.feedSeq());
		FeedEntity feedEntity = feedRepository.findByCreatorMemberAndFeedSeq(memberEntity, requestDto.feedSeq());

		/*
		 1. Feed Image 삭제
		 2. HIDE FEED 삭제
		 3. BOOK MARK FEED 삭제
		*/
		feedImageRepository.deleteByFeedEntity(feedEntity);
		hideFeedRepository.deleteByFeedEntity(feedEntity);
		bookMarkRepository.deleteByFeedEntity(feedEntity);

		// Feed 삭제
		feedRepository.deleteFeedEntity(feedEntity);
	}
}

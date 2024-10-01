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

						return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, List.of(scheduleFeedInfo));
					})
					.orElse(null);
			})
			.filter(Objects::nonNull)
			.stream().collect(Collectors.collectingAndThen(Collectors.toList(), list ->
				new PageImpl<>(list, pageable, list.size())));
	}

	@Transactional(readOnly = true)
	public GetFeedResponseDto getFeed(Long memberSeq, Long feedSeq) {
		/*
		 1. Member 유효성 검사
		 2. Feed 유효성 검사
		 3. Member Schedule 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		FeedEntity feedEntity = feedRepository.findByFeedSeq(feedSeq);
		ScheduleEntity scheduleEntity = feedEntity.getSchedule();
		scheduleMemberRepository.findAcceptedScheduleMemberInSchedule(scheduleEntity.getScheduleSeq(), memberSeq);

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

		return feedEntityMapper.toGetFeedResponseDto(scheduleInfo, scheduleFeedInfos);
	}
}

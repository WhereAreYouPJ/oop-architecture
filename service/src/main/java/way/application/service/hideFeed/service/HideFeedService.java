package way.application.service.hideFeed.service;

import static way.application.service.hideFeed.dto.request.HideFeedRequestDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.hideFeed.repository.HideFeedRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.infrastructure.schedule.entity.ScheduleEntity;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;
import way.application.infrastructure.scheduleMember.repository.ScheduleMemberRepository;
import way.application.service.hideFeed.mapper.HideFeedMapper;

@Service
@RequiredArgsConstructor
public class HideFeedService {
	private final HideFeedRepository hideFeedRepository;
	private final MemberRepository memberRepository;
	private final ScheduleMemberRepository scheduleMemberRepository;

	private final HideFeedMapper hideFeedMapper;

	@Transactional
	public void hideFeed(AddHideFeedRequestDto hideFeedRequestDto) {
		/*
		 예외처리 (Repo 단)
		 1. member 가 수락한 Schedule 인지 확인
		 2. member 확인
		*/
		MemberEntity hideMemberEntity = memberRepository.findByMemberSeq(hideFeedRequestDto.memberSeq());
		ScheduleMemberEntity scheduleMemberEntity
			= scheduleMemberRepository.findAcceptedScheduleMemberByScheduleSeqAndMemberSeq(
			hideFeedRequestDto.scheduleSeq(),
			hideFeedRequestDto.memberSeq()
		);
		ScheduleEntity hideScheduleEntity = scheduleMemberEntity.getSchedule();

		// 저장
		hideFeedRepository.save(hideFeedMapper.toHideFeedEntity(hideScheduleEntity, hideMemberEntity));
	}
}

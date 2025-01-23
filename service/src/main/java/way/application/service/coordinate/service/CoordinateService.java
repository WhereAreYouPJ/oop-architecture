package way.application.service.coordinate.service;

import static way.application.service.coordinate.dto.request.CoordinateRequestDto.*;
import static way.application.service.coordinate.dto.response.CoordinateResponseDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.domain.schedule.ScheduleDomain;
import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.coordinate.repository.CoordinateRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;
import way.application.infrastructure.jpa.schedule.repository.ScheduleRepository;
import way.application.service.coordinate.mapper.CoordinateEntityMapper;

@Service
@RequiredArgsConstructor
public class CoordinateService {
	private final MemberRepository memberRepository;
	private final ScheduleRepository scheduleRepository;
	private final CoordinateRepository coordinateRepository;

	private final CoordinateEntityMapper coordinateEntityMapper;

	private final ScheduleDomain scheduleDomain;

	@Transactional
	public void createCoordinate(CreateCoordinateRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		 2. Schedule 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(requestDto.scheduleSeq());

		CoordinateEntity coordinateEntity
			= coordinateRepository.findOptionalCoordinateByMemberEntity(memberEntity, scheduleEntity)
			.orElseGet(() -> coordinateEntityMapper.toCoordinateEntity(memberEntity, scheduleEntity, requestDto));

		// 좌표 정보 업데이트 및 저장
		coordinateEntity.updateCoordinate(requestDto.x(), requestDto.y());
		coordinateRepository.saveCoordinateEntity(coordinateEntity);
	}

	@Transactional(readOnly = true)
	public GetCoordinateResponseDto getCoordinate(Long memberSeq, Long scheduleSeq) {
		/*
		 1. Member 유효성
		 2. Schedule 유효성
		 3. 시간 유효성 검사
		 4. Coordinate 유효성
		 5. 하루 종일 여부 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		ScheduleEntity scheduleEntity = scheduleRepository.findByScheduleSeq(scheduleSeq);
		scheduleDomain.validateScheduleStartTime(scheduleEntity.getStartTime());
		CoordinateEntity coordinateEntity = coordinateRepository.findByMemberEntity(memberEntity, scheduleEntity);
		scheduleDomain.validateAllDay(scheduleEntity);

		return coordinateEntityMapper.toGetCoordinateResponseDto(memberEntity, coordinateEntity);
	}
}

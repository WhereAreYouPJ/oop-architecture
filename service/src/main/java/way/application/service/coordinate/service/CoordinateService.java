package way.application.service.coordinate.service;

import static way.application.service.coordinate.dto.request.CoordinateRequestDto.*;
import static way.application.service.coordinate.dto.response.CoordinateResponseDto.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.coordinate.entity.CoordinateEntity;
import way.application.infrastructure.jpa.coordinate.repository.CoordinateRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.service.coordinate.mapper.CoordinateEntityMapper;

@Service
@RequiredArgsConstructor
public class CoordinateService {
	private final MemberRepository memberRepository;
	private final CoordinateRepository coordinateRepository;

	private final CoordinateEntityMapper coordinateEntityMapper;

	@Transactional
	public void createCoordinate(CreateCoordinateRequestDto requestDto) {
		/*
		 1. Member 유효성 검사
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());

		CoordinateEntity coordinateEntity = coordinateRepository.findOptionalCoordinateByMemberEntity(memberEntity)
			.orElseGet(() -> coordinateEntityMapper.toCoordinateEntity(memberEntity, requestDto));

		// 좌표 정보 업데이트 및 저장
		coordinateEntity.updateCoordinate(requestDto.x(), requestDto.y());
		coordinateRepository.saveCoordinateEntity(coordinateEntity);
	}

	@Transactional(readOnly = true)
	public GetCoordinateResponseDto getCoordinate(Long memberSeq) {
		/*
		 1. Member 유효성
		 2. Coordinate 유효성
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);
		CoordinateEntity coordinateEntity = coordinateRepository.findByMemberEntity(memberEntity);

		return coordinateEntityMapper.toGetCoordinateResponseDto(memberEntity, coordinateEntity);
	}
}

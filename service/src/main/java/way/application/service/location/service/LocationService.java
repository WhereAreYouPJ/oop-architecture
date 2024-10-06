package way.application.service.location.service;

import static way.application.service.location.dto.request.LocationRequestDto.*;
import static way.application.service.location.dto.response.LocationResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.location.entity.LocationEntity;
import way.application.infrastructure.jpa.location.repository.LocationRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;
import way.application.service.location.mapper.LocationEntityMapper;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final MemberRepository memberRepository;
	private final LocationRepository locationRepository;

	private final LocationEntityMapper locationMapper;

	@Transactional
	public AddLocationResponseDto addLocation(AddLocationRequestDto requestDto) {
		/*
		 1. Member 존재 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());

		// 저장
		LocationEntity locationEntity = locationRepository.saveLocationEntity(
			locationMapper.toLocationMapper(memberEntity, requestDto.location(), requestDto.streetName())
		);

		return locationMapper.toAddLocationResponseDto(locationEntity);
	}

	@Transactional
	public void deleteLocation(DeleteLocationRequestDto requestDto) {
		/*
		 1. Member 존재 확인
		 2. Location 존재 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(requestDto.memberSeq());
		List<LocationEntity> locationEntityList
			= locationRepository.findAllByMemberEntityAndLocationSeqs(memberEntity, requestDto.locationSeqs());

		locationRepository.deleteAll(locationEntityList);
	}

	@Transactional(readOnly = true)
	public List<GetLocationResponseDto> getLocation(Long memberSeq) {
		/*
		 1. Member 유효성 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		return locationRepository.findByMemberEntity(memberEntity)
			.stream()
			.map(locationMapper::toGetLocationResponseDto)
			.collect(Collectors.toList());
	}
}

package way.application.service.location.service;

import static way.application.service.location.dto.request.LocationRequestDto.*;
import static way.application.service.location.dto.response.LocationResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.location.entity.LocationEntity;
import way.application.infrastructure.location.repository.LocationRepository;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.service.location.mapper.LocationMapper;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final MemberRepository memberRepository;
	private final LocationRepository locationRepository;

	private final LocationMapper locationMapper;

	@Transactional
	public AddLocationResponseDto addLocation(AddLocationRequestDto addLocationRequestDto) {
		/*
		 1. Member 존재 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(addLocationRequestDto.memberSeq());

		// 저장
		LocationEntity locationEntity = locationRepository.saveLocationEntity(
			locationMapper.toLocationMapper(
				memberEntity,
				addLocationRequestDto.location(),
				addLocationRequestDto.streetName()
			)
		);

		return new AddLocationResponseDto(locationEntity.getLocationSeq());
	}

	@Transactional
	public void deleteLocation(DeleteLocationRequestDto deleteLocationRequestDto) {
		/*
		 1. Member 존재 확인
		 2. Location 존재 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(deleteLocationRequestDto.memberSeq());
		List<LocationEntity> locationEntities = locationRepository.findAllByMemberEntityAndLocationSeqs(
			memberEntity,
			deleteLocationRequestDto.locationSeqs()
		);

		locationRepository.deleteAll(locationEntities);
	}

	@Transactional(readOnly = true)
	public List<GetLocationResponseDto> getLocation(Long memberSeq) {
		/*
		 1. Member 유효성 확인
		*/
		MemberEntity memberEntity = memberRepository.findByMemberSeq(memberSeq);

		// Location 조회 -> member
		return locationRepository.findByMemberEntity(memberEntity)
			.stream()
			.map(locationEntity -> new GetLocationResponseDto(
				locationEntity.getLocationSeq(),
				locationEntity.getLocation(),
				locationEntity.getStreetName()
			))
			.collect(Collectors.toList());
	}
}

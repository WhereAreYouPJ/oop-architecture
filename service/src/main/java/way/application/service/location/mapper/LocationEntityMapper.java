package way.application.service.location.mapper;

import static way.application.service.location.dto.response.LocationResponseDto.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.location.entity.LocationEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationEntityMapper {
	@Mapping(target = "locationSeq", ignore = true)
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "location", source = "location")
	@Mapping(target = "streetName", source = "streetName")
	LocationEntity toLocationMapper(MemberEntity memberEntity, String location, String streetName);

	AddLocationResponseDto toAddLocationResponseDto(LocationEntity locationEntity);

	GetLocationResponseDto toGetLocationResponseDto(LocationEntity locationEntity);
}

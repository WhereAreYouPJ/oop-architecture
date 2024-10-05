package way.application.service.member.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

import static way.application.service.member.dto.request.MemberRequestDto.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface MemberMapper {

	@Mapping(target = "memberSeq", ignore = true)
	MemberEntity toMemberEntity(SaveMemberRequestDto request, String encodedPassword, String memberCode);

	@Mapping(target = "memberSeq", ignore = true)
	MemberEntity toKakaoMemberEntity(SaveSnsMemberRequestDto request, String kakaoPassword, String memberCode);

	@Mapping(target = "memberSeq", ignore = true)
	MemberEntity toAppleMemberEntity(SaveSnsMemberRequestDto request, String applePassword, String memberCode);

}

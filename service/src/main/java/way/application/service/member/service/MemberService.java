package way.application.service.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.member.repository.MemberRepository;
import way.application.service.member.dto.request.MemberRequestDto;
import way.application.service.member.dto.request.MemberRequestDto.SaveMemberRequestDto;
import way.application.service.member.dto.response.MemberResponseDto;
import way.application.service.member.mapper.MemberMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static way.application.service.member.dto.response.MemberResponseDto.*;


@Service
@RequiredArgsConstructor
public class MemberService {
	/**
	 * 유효성 검사 -> Repository Interface 에서 처리
	 * 비즈니스 로직 -> Domain 단에서 처리
	 * Service 로직 -> Domain 호출 및 저장
	 *
	 * Service Layer -> Repo의 CRUD만 처리
	 */

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;
	private final BCryptPasswordEncoder encoder;

	@Transactional
	public void saveMember(SaveMemberRequestDto saveMemberRequestDto) {

		//멤버 유효성 검사
		memberRepository.isDuplicatedUserId(saveMemberRequestDto.userId());

		// Member 저장
		memberRepository.saveMember(
				memberMapper.toMemberEntity(saveMemberRequestDto,encoder.encode(saveMemberRequestDto.password()))
		);


	}

	public CheckIdResponseDto checkId(MemberRequestDto.CheckIdRequestDto checkIdRequestDto) {

		//userId 중복 검사
		memberRepository.isDuplicatedUserId(checkIdRequestDto.userId());

		return new CheckIdResponseDto(checkIdRequestDto.userId());

	}
}

package way.application.infrastructure.base.data;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;
import way.application.infrastructure.jpa.admin.repository.AdminImageRepository;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class DataLoader {
	private final BCryptPasswordEncoder encoder;

	@Bean
	ApplicationRunner init(MemberRepository memberRepository, AdminImageRepository adminImageRepository) {
		return args -> {
			// 첫 번째 더미 데이터
			MemberEntity member1 = MemberEntity.builder()
				.userName("zxcv")
				.encodedPassword(encoder.encode("zxcv1234!@#$"))
				.email("zxcv@zxcv.com")
				.memberCode("zxcv")
				.fireBaseTargetToken("zxcv")
				.build();
			memberRepository.saveMember(member1);

			// 두 번째 더미 데이터
			MemberEntity member2 = MemberEntity.builder()
				.userName("asdf")
				.encodedPassword(encoder.encode("asdf1234!@#$"))
				.email("asdf@asdf.com")
				.memberCode("asdf")
				.fireBaseTargetToken("asdf")
				.build();
			memberRepository.saveMember(member2);

			// 세 번째 더미 데이터
			MemberEntity member3 = MemberEntity.builder()
				.userName("qwer")
				.encodedPassword(encoder.encode("qwer1234!@#$"))
				.email("qwer@qwer.com")
				.memberCode("qwer")
				.fireBaseTargetToken("qwer")
				.build();
			memberRepository.saveMember(member3);

			// 네 번째 더미 데이터
			MemberEntity member4 = MemberEntity.builder()
				.userName("uiop")
				.encodedPassword(encoder.encode("uiop1234!@#$"))
				.email("uiop@uiop.com")
				.memberCode("uiop")
				.fireBaseTargetToken("uiop")
				.build();
			memberRepository.saveMember(member4);

			// 홈 화면 데미 데이터
			AdminImageEntity adminImageEntity = AdminImageEntity.builder()
				.adminImageSeq(1L)
				.imageURL("https://way-s3.s3.ap-northeast-2.amazonaws.com/990e3058-e9c9-4629-982c-420bf818b415.jpg")
				.build();
			adminImageRepository.saveAdminEntity(adminImageEntity);
		};
	}
}
package way.application.infrastructure.base.data;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class DataLoader {
	private final BCryptPasswordEncoder encoder;

	@Bean
	ApplicationRunner init(MemberRepository memberRepository) {
		return args -> {
			// 첫 번째 더미 데이터
			MemberEntity member1 = MemberEntity.builder()
				.userName("zxcv")
				.encodedPassword(encoder.encode("zxcv1234!@#$"))
				.email("zxcv@zxcv.com")
				.build();
			memberRepository.saveMember(member1);

			// 두 번째 더미 데이터
			MemberEntity member2 = MemberEntity.builder()
				.userName("asdf")
				.encodedPassword(encoder.encode("asdf1234!@#$"))
				.email("asdf@asdf.com")
				.build();
			memberRepository.saveMember(member2);

			// 세 번째 더미 데이터
			MemberEntity member3 = MemberEntity.builder()
				.userName("qwer")
				.encodedPassword(encoder.encode("qwer1234!@#$"))
				.email("qwer@qwer.com")
				.build();
			memberRepository.saveMember(member3);

			// 네 번째 더미 데이터
			MemberEntity member4 = MemberEntity.builder()
				.userName("uiop")
				.encodedPassword(encoder.encode("uiop1234!@#$"))
				.email("uiop@uiop.com")
				.build();
			memberRepository.saveMember(member4);
		};
	}
}
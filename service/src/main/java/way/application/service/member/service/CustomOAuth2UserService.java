package way.application.service.member.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 카카오로부터 받은 사용자 정보 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "kakao"
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // "id"

        // 사용자 정보 매핑 (카카오 응답 구조에 따라 다를 수 있음)
        Long id = oAuth2User.getAttribute("id");
        java.util.Map<String, Object> properties = oAuth2User.getAttribute("properties");
        java.util.Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 추출한 정보로 서비스의 User 객체 생성 또는 조회
        // ...

        return oAuth2User; // 또는 CustomOAuth2User 객체 생성하여 반환
    }
}
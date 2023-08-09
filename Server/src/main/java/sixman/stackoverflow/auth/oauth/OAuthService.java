package sixman.stackoverflow.auth.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth 서비스 이름(ex. kakao, naver, google)

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes()); // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스

        if(registrationId.equals("github")){
            attributes.put("email", getEmail(userRequest.getAccessToken().getTokenValue()));
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                                .getProviderDetails()
                                .getUserInfoEndpoint()
                                .getUserNameAttributeName(); // OAuth2 로그인 진행 시 키가 되는 필드값



        MemberProfile memberProfile = OAuthAttributes.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌

        Member member = getMember(memberProfile);
        if (member == null) {
            member = saveMember(memberProfile);
        }

        Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName, memberProfile, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().toString())),
                customAttribute,
                userNameAttributeName);
    }

    private Map customAttribute(Map attributes, String userNameAttributeName, MemberProfile memberProfile, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", memberProfile.getName());
        customAttribute.put("email", memberProfile.getEmail());
        return customAttribute;

    }

    private Member getMember(MemberProfile memberProfile) {

        Member member = memberRepository.findByEmail(memberProfile.getEmail())
                .orElse(null);

        return member;
    }

    private Member saveMember(MemberProfile memberProfile) {
        Member member = memberProfile.toMember();
        return memberRepository.save(member);
    }

    private String getEmail(String accessToken){
        String emailUrl = "https://api.github.com/user/emails";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GithubEmail[]> response = restTemplate.exchange(emailUrl, HttpMethod.GET, entity, GithubEmail[].class);

        if(response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0].getEmail();
        }

        return null;
    }

    @Getter
    @Setter
    public static class GithubEmail {
        private String email;
        private boolean primary;
        private boolean verified;
        private String visibility;
    }
}

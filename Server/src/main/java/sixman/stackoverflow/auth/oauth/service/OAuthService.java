package sixman.stackoverflow.auth.oauth.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sixman.stackoverflow.auth.jwt.dto.Token;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.auth.utils.AuthConstant;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class OAuthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public OAuthService(InMemoryClientRegistrationRepository inMemoryRepository, MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.inMemoryRepository = inMemoryRepository;
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }


    @Transactional
    public Token login(Provider provider, String code) {

        ClientRegistration clientRegistration = inMemoryRepository.findByRegistrationId(provider.getDescription());

        String token = getToken(code, clientRegistration);

        OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse.withToken(token)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(3600L)
                .build();

        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, tokenResponse.getAccessToken());

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth 서비스 이름(ex. kakao, naver, google)

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes()); // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스

        if(registrationId.equals("github")){
            attributes.put("email", getEmail(userRequest.getAccessToken().getTokenValue()));
        }

        MemberProfile memberProfile = Provider.extract(registrationId, attributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌

        Member member = getMember(memberProfile);
        if (member == null) {
            member = saveMember(memberProfile);
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                member.getMemberId(),
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().toString())));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String accessToken = tokenProvider.generateAccessToken(authentication, AuthConstant.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateRefreshToken(authentication, AuthConstant.ACCESS_TOKEN_EXPIRE_TIME);

        return new Token(accessToken, refreshToken, member.getMemberId());
    }

    private Member getMember(MemberProfile memberProfile) {

        return memberRepository.findByEmail(memberProfile.getEmail())
                .orElse(null);
    }

    private Member saveMember(MemberProfile memberProfile) {
        Member member = Member.createMember(
                memberProfile.getEmail(),
                memberProfile.getEmail().split("@")[0],
                "oauthUser");
        return memberRepository.save(member);
    }

    private String getToken(String code, ClientRegistration clientRegistration) {

        RestTemplate restTemplate = new RestTemplate();

        String uri = clientRegistration.getProviderDetails().getTokenUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

        HttpEntity entity = new HttpEntity<>(tokenRequest(code, clientRegistration), headers);

        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        return responseEntity.getBody().get("access_token");
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

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id",provider.getClientId());
        return formData;
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

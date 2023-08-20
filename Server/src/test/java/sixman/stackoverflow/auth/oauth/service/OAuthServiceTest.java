package sixman.stackoverflow.auth.oauth.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import sixman.stackoverflow.auth.jwt.dto.Token;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

class OAuthServiceTest extends ServiceTest {

    @Autowired OAuthService oAuthService;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("OAuth 로그인을 하면 자동으로 회원가입된 후 토큰을 발급하고 memberId 를 리턴한다.")
    void loginAndSignup() {
        //given
        Provider provider = Provider.GOOGLE;
        String code = "code";
        String email = "test@google.com";

        setTokenExchangeMock();
        setProfileExchangeMock(email);

        //when
        Token token = oAuthService.login(provider, code);

        //then
        Member member = memberRepository.findById(token.getMemberId()).orElseThrow();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();


    }

    @Test
    @DisplayName("OAuth 회원가입이 된 member 가 로그인을 하면 자동으로 로그인이 되고 토큰이 발급된다.")
    void login() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Provider provider = Provider.GOOGLE;
        String code = "code";

        setTokenExchangeMock();
        setProfileExchangeMock(member.getEmail());

        //when
        Token token = oAuthService.login(provider, code);

        //then
        Member findMember = memberRepository.findById(token.getMemberId()).orElseThrow();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getRefreshToken()).isNotNull();

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1); // 회원가입이 되어 있으면 추가로 되지 않는다.


    }

    private void setTokenExchangeMock() {
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "mocked_access_token");

        ResponseEntity<Map<String, String>> mockEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        given(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        ))
                .willReturn(mockEntity);
    }

    private void setProfileExchangeMock(String email) {

        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                Collections.singletonMap("email", email),
                "email"
        );

        given(defaultOAuth2UserService.loadUser(any(OAuth2UserRequest.class)))
                .willReturn(defaultOAuth2User);
    }
}
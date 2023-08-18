package sixman.stackoverflow.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.auth.jwt.dto.Token;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.auth.oauth.service.Provider;
import sixman.stackoverflow.auth.oauth.service.OAuthService;
import sixman.stackoverflow.domain.member.controller.dto.MemberMailAuthApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberMailConfirmApiRequest;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import javax.persistence.EntityManager;

import java.util.Collections;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureRestDocs
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class})
@ActiveProfiles("local")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EntityManager em;
    @Autowired private TokenProvider tokenProvider;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private OAuthService oAuthService;

    @Test
    @DisplayName("일반 로그인 API")
    void jwtLogin() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("email@test.com", "1234abcd!");
        createMember(loginDto.getEmail(), loginDto.getPassword());

        String content = objectMapper.writeValueAsString(loginDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(content));


        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("memberId").exists());

        //restDocs
        actions.andDo(
                document("auth/login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").description("로그인할 이메일"),
                                fieldWithPath("password").description("로그인할 비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("로그인한 회원의 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("OAuth 로그인 API")
    void oauthLogin() throws Exception {
        //given
        String provider = "GOOGLE";
        String code = "DNIL345AS21GN34";
        Long memberId = 1L;

        Token token = new Token("Bearer accessToken", "Bearer refreshToken", memberId);
        given(oAuthService.login(any(Provider.class), anyString())).willReturn(token);

        //when
        ResultActions actions = mockMvc.perform(
                get("/auth/oauth")
                        .param("provider", provider)
                        .param("code", code));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("Refresh"))
                .andExpect(jsonPath("memberId").value(memberId));

        //restDocs
        actions.andDo(
                document("auth/oauth",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("provider").description(generateLinkCode(Provider.class)),
                                parameterWithName("code").description("OAuth 인증 코드")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("accessToken"),
                                headerWithName("Refresh").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("로그인한 회원의 ID")
                        )
                )
        );

    }

    @Test
    @DisplayName("RefreshToken 발급 API")
    void refreshIssue() throws Exception {
        //given
        Member member = createMember("test@test.com", "1234abcd!!");
        String refreshToken = createRefreshToken(member, 10000L);

        //when
        ResultActions actions = mockMvc.perform(post("/auth/refresh")
                .header("Refresh", "Bearer " + refreshToken)
                .contentType(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));

        //restdocs
        actions.andDo(
                document("auth/refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Refresh").description("refreshToken")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("accessToken")
                        )
                )
        );
    }



    private Member createMember(String email, String password) {

        Member member = Member.builder()
                .email(email)
                .nickname("nickName")
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();

        em.persist(member);

        return member;
    }

    private String createAccessToken(Member member, long accessTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateAccessToken(authenticationToken, accessTokenExpireTime);
    }

    private String createRefreshToken(Member member, long refreshTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateRefreshToken(authenticationToken, refreshTokenExpireTime);
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(
                member.getMemberId(),
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    private Attributes.Attribute getFormat(
            final String value){
        return new Attributes.Attribute("format",value);
    }

    private String generateLinkCode(Class<?> clazz) {
        return String.format("link:../common/%s.html[%s 값 보기,role=\"popup\"]",
                clazz.getSimpleName().toLowerCase(), clazz.getSimpleName());
    }

}
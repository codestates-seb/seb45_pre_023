package sixman.stackoverflow.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.auth.oauth.service.Provider;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberFindPasswordApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberMailAuthApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberMailConfirmApiRequest;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.questionrecommend.repository.QuestionRecommendRepository;
import sixman.stackoverflow.domain.questiontag.QuestionTagRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.tag.repository.TagRepository;
import sixman.stackoverflow.module.email.service.MailService;
import sixman.stackoverflow.module.redis.service.RedisService;

import javax.persistence.EntityManager;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("회원 인증/가입 통합 테스트")
@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected MemberRepository memberRepository;
    @Autowired protected PasswordEncoder passwordEncoder;

    @MockBean
    protected MailService mailService;
    @MockBean protected RedisService redisService;
    @MockBean protected RestTemplate restTemplate;
    @MockBean protected DefaultOAuth2UserService defaultOAuth2UserService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @TestFactory
    @DisplayName("회원가입 성공 테스트")
    Collection<DynamicTest> signup() {

        String email = "test@google.com";
        String password = "1q2w3e4r!";
        String nickname = "test";

        String givenCode = "123456";

        return List.of(
                dynamicTest("1. 이메일 인증을 받는다.", () -> {
                    //given
                    given(mailService.sendAuthEmail(email)).willReturn(givenCode);

                    MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                            .email(email)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/members/email")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("2. 이메일 인증 코드를 확인한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn(givenCode);

                    MemberMailConfirmApiRequest request = MemberMailConfirmApiRequest.builder()
                            .email(email)
                            .code(givenCode)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/members/email/confirm")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data").value("true"));
                }),
                dynamicTest("3. 이메일 인증 이후 email, password, nickname 으로 회원 가입을 한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn("true");

                    MemberCreateApiRequest request = MemberCreateApiRequest.builder()
                            .email(email)
                            .nickname(nickname)
                            .password(password)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/auth/signup")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isCreated())
                            .andExpect(header().string("Location", startsWith("/members/")));
                })
        );
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일이 중복될 때 이메일 인증 요청이 409 에러를 반환한다.")
    void signupDuplicate() throws Exception {
        //given
        String email = "test@google.com";
        createAndSaveMember(email);

        MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                .email(email)
                .build();

        //when
        ResultActions actions = mockMvc.perform(post("/members/email")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 회원 이메일입니다."));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 인증 요청 시 휴면 회원일 때 401 에러를 반환한다.")
    void signupDisable() throws Exception {
        //given
        String email = "test@google.com";
        createAndSaveMemberDisable(email);

        MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                .email(email)
                .build();

        //when
        ResultActions actions = mockMvc.perform(post("/members/email")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("탈퇴한 회원입니다."));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 인증 요청을 하지 않고 이메일 인증 확인(confirm) 을 하려고 하면 400 에러를 반환한다.")
    void signupEmailAuthNotAttempt() throws Exception {
        //given
        String email = "test@google.com";
        String givenCode = "123456";

        MemberMailConfirmApiRequest request = MemberMailConfirmApiRequest.builder()
                .email(email)
                .code(givenCode)
                .build();

        given(redisService.getValues(anyString())).willReturn(null);

        //when
        ResultActions actions = mockMvc.perform(post("/members/email/confirm")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 인증을 먼저 시도해주세요."));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 인증을 완료하지 않았을 때 회원가입을 시도하면 401 에러를 반환한다.")
    void signupEmailAuthNotComplete() throws Exception {
        //given
        String email = "test@google.com";
        String nickname = "test";
        String password = "1q2w3e4r!";

        MemberCreateApiRequest request = MemberCreateApiRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        given(redisService.getValues(anyString())).willReturn("123456"); // 이메일 인증을 완료하지 않은 상태

        //when
        ResultActions actions = mockMvc.perform(post("/auth/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("이메일 인증이 완료되지 않았습니다."));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login() throws Exception {
        //given
        String email = "test@google.com";
        String password = "1q2w3e4r!";
        Member member = createAndSaveMember(email, password);

        LoginDto request = new LoginDto(email, password);

        //when
        ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getMemberId()))
                .andExpect(header().string("Authorization", startsWith("Bearer ")))
                .andExpect(header().string("Refresh", startsWith("Bearer ")));
    }

    @TestFactory
    @DisplayName("로그인 실패 테스트")
    Collection<DynamicTest> loginFail() throws Exception {
        //given
        String email = "test@google.com";
        String password = "1q2w3e4r!";
        Member member = createAndSaveMember(email, password);

        LoginDto request = new LoginDto(email, password);

        //when
        ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getMemberId()))
                .andExpect(header().string("Authorization", startsWith("Bearer ")))
                .andExpect(header().string("Refresh", startsWith("Bearer ")));

        return List.of(
                dynamicTest("email 이 다르면 로그인에 실패한다.", () -> {
                    //given
                    LoginDto requestFail = new LoginDto("a" + email, password);

                    //when
                    ResultActions actionsFail = mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFail)));

                    //then
                    actionsFail
                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.message").value("로그인 정보를 확인해주세요."));
                }),
                dynamicTest("password 가 다르면 로그인에 실패한다.", () -> {
                    //given
                    LoginDto requestFail = new LoginDto(email, "a" + password);

                    //when
                    ResultActions actionsFail = mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFail)));

                    //then
                    actionsFail
                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.message").value("로그인 정보를 확인해주세요."));
                }),
                dynamicTest("휴면 회원이면 로그인에 실패한다. (message = \"탈퇴한 회원입니다.\")", () -> {
                    //given
                    member.disable(); // 휴면 회원으로 변경
                    memberRepository.save(member);

                    LoginDto requestFail = new LoginDto(email, password);

                    //when
                    ResultActions actionsFail = mockMvc.perform(post("/auth/login")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestFail)));

                    //then
                    actionsFail
                            .andDo(print())
                            .andExpect(status().isUnauthorized())
                            .andExpect(jsonPath("$.message").value("탈퇴한 회원입니다."));
                })
        );
    }

    @TestFactory
    @DisplayName("비밀번호 찾기 성공 테스트 - 일반 회원")
    Collection<DynamicTest> findPassword() {
        //given
        String email = "test@google.com";
        createAndSaveMember(email);

        String givenCode = "123456";

        return List.of(
                dynamicTest("1. 이메일 인증을 받는다.", () -> {
                    //given
                    given(mailService.sendAuthEmail(email)).willReturn(givenCode);

                    MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                            .email(email)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/auth/email")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("2. 이메일 인증 코드를 확인한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn(givenCode);

                    MemberMailConfirmApiRequest request = MemberMailConfirmApiRequest.builder()
                            .email(email)
                            .code(givenCode)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/auth/email/confirm")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data").value("true"));
                }),
                dynamicTest("3. 이메일 인증 이후 패스워드를 변경한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn("true");

                    MemberFindPasswordApiRequest request = MemberFindPasswordApiRequest.builder()
                            .email(email)
                            .password("1q2w3e4r!")
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(patch("/auth/password")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                })
        );
    }

    @TestFactory
    @DisplayName("비밀번호 찾기 성공 테스트 - 휴면 회원")
    Collection<DynamicTest> findPasswordDisable() {
        //given
        String email = "test@google.com";
        createAndSaveMemberDisable(email);

        String givenCode = "123456";

        return List.of(
                dynamicTest("1. 이메일 인증을 받는다.", () -> {
                    //given
                    given(mailService.sendAuthEmail(email)).willReturn(givenCode);

                    MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                            .email(email)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/auth/email")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("2. 이메일 인증 코드를 확인한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn(givenCode);

                    MemberMailConfirmApiRequest request = MemberMailConfirmApiRequest.builder()
                            .email(email)
                            .code(givenCode)
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(post("/auth/email/confirm")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data").value("true"));
                }),
                dynamicTest("3. 이메일 인증 이후 패스워드를 변경한다.", () -> {
                    //given
                    given(redisService.getValues(anyString())).willReturn("true");

                    MemberFindPasswordApiRequest request = MemberFindPasswordApiRequest.builder()
                            .email(email)
                            .password("1q2w3e4r!")
                            .build();

                    //when
                    ResultActions actions = mockMvc.perform(patch("/auth/password")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                })
        );
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 비밀번호를 찾기 위해 이메일 인증 요청 시 없는 이메일로 요청하면 실패한다.")
    void findPasswordMemberNotFound() throws Exception {
        //given
        String email = "notSavedEmail@google.com";
        String givenCode = "123456";

        MemberMailAuthApiRequest request = MemberMailAuthApiRequest.builder()
                .email(email)
                .build();

        given(mailService.sendAuthEmail(email)).willReturn(givenCode);

        //when
        ResultActions actions = mockMvc.perform(post("/auth/email")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 이메일 인증 요청을 하지 않고 이메일 인증 확인(confirm) 을 하려고 하면 400 에러를 반환한다.")
    void findPasswordEmailAuthNotAttempt() throws Exception {

        String email = "notSavedEmail@google.com";
        String givenCode = "123456";

        createAndSaveMember(email);

        //given
        given(redisService.getValues(anyString())).willReturn(null);

        MemberMailConfirmApiRequest request = MemberMailConfirmApiRequest.builder()
                .email(email)
                .code(givenCode)
                .build();

        //when
        ResultActions actions = mockMvc.perform(post("/auth/email/confirm")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 인증을 먼저 시도해주세요."));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 이메일 인증을 완료하지 않았을 때 비밀번호 찾기를 시도하면 401 에러를 반환한다.")
    void findPasswordEmailAuthNotComplete() throws Exception {

        String email = "test@google.com";
        String givenCode = "123456";

        createAndSaveMember(email);

        //given
        given(redisService.getValues(anyString())).willReturn(givenCode); // 이메일 인증을 완료하지 않으면 redis 에서 true 를 반환하지 않는다.

        MemberFindPasswordApiRequest request = MemberFindPasswordApiRequest.builder()
                .email(email)
                .password("1q2w3e4r!")
                .build();

        //when
        ResultActions actions = mockMvc.perform(patch("/auth/password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("이메일 인증이 완료되지 않았습니다."));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 테스트 - 이메일 인증은 완료했지만 다른 이메일로 변경하여 비밀번호 찾기를 시도하면 실패한다.")
    void findPasswordEmailMemberNotFound() throws Exception {

        String email = "test@google.com";

        createAndSaveMember(email);

        //given
        given(redisService.getValues("AuthCode test@google.com")).willReturn("true"); // 이메일 인증은 완료

        MemberFindPasswordApiRequest request = MemberFindPasswordApiRequest.builder()
                .email("a" + email) // 다른 이메일로 요청
                .password("1q2w3e4r!")
                .build();

        //when
        ResultActions actions = mockMvc.perform(patch("/auth/password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }

    @TestFactory
    @DisplayName("OAuth 로그인 성공 테스트")
    Collection<DynamicTest> oauthLoginAndSignup() throws Exception {
        //given
        Provider provider = Provider.GOOGLE;
        String code = "code";
        String email = "test@google.com";

        setTokenExchangeMock();
        setProfileExchangeMock(email);

        return List.of(
            dynamicTest("최초 로그인 시 회원가입을 추가로 진행한다.", () -> {
                //when
                ResultActions actions = mockMvc.perform(get("/auth/oauth")
                        .param("provider", provider.name())
                        .param("code", code));

                //then
                actions
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.memberId").exists());

                // 회원가입이 정상적으로 진행되었는지 확인
                boolean isExist = memberRepository.findByEmail(email).isPresent();
                assertThat(isExist).isTrue();
            }),
            dynamicTest("두 번째 로그인부터 회원가입은 진행되지 않지만 정상 로그인된다.", () -> {
                //when
                ResultActions actions = mockMvc.perform(get("/auth/oauth")
                        .param("provider", provider.name())
                        .param("code", code));

                //then
                actions
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.memberId").exists());

                //같은 email 로 중복 회원가입이 진행되지 않았는지 확인 -> 중복 저장되었으면 Exception 발생
                boolean exist = memberRepository.findByEmail(email).isPresent();
                assertThat(exist).isTrue();
            }),
            dynamicTest("해당 email 을 탈퇴(disable) 한 후 다시 로그인해도 자동으로 정상 로그인된다.", () -> {
                //given
                Member member = memberRepository.findByEmail(email).get();
                member.disable();
                memberRepository.flush();

                //when
                ResultActions actions = mockMvc.perform(get("/auth/oauth")
                        .param("provider", provider.name())
                        .param("code", code));

                //then
                actions
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.memberId").exists());

                //같은 email 로 중복 회원가입이 진행되지 않았는지 확인 -> 중복 저장되었으면 Exception 발생
                boolean exist = memberRepository.findByEmail(email).isPresent();
                assertThat(exist).isTrue();
            })
        );
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


    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(
                member.getMemberId(),
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    private Member createAndSaveMember(String email){

        Member member = createMember(email);

        return memberRepository.save(member);
    }

    private Member createAndSaveMember(String email, String password){

        Member member = createMember(email);
        member.updatePassword(passwordEncoder.encode(password));

        return memberRepository.save(member);
    }

    private Member createAndSaveMemberDisable(String email){

        Member member = createMember(email);
        member.disable();

        return memberRepository.save(member);
    }

    private Member createMember(String email) {
        Member member = Member.builder()
                .email(email)
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();
        return member;
    }
}

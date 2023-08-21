package sixman.stackoverflow.integration;

import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.support.StandardServletPartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.controller.dto.MemberPasswordUpdateAPiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberUpdateApiRequest;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.response.PageInfo;
import sixman.stackoverflow.module.aws.service.S3Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("회원 조회, 수정, 삭제 통합 테스트")
public class MemberIntegrationTest extends IntegrationTest{

    @Autowired S3Service s3Service;

    Member member;
    String memberPassword = "1q2w3e4r!";
    Member otherMember;
    String otherMemberPassword = "1q2w3e4r!";
    String accessToken;
    String otherAccessToken;
    List<Question> questions = new ArrayList<>();
    List<Answer> answers = new ArrayList<>();
    List<Reply> replies = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();

    @BeforeAll
    void beforeAll() throws Exception {
        /**
         * 최초 데이터
         * 1. 회원 2명
         * 2. 각 회원 당 질문 20 개 작성
         * 3. 질문 당 태그 2개
         * 4. member 는 otherMember 질문에 1개 씩 답변 1개씩 작성 (총 20개)
         * 5. member 는 본인 답변 1개 당 댓글 10개 작성 (총 200개)
         * 6. 추천, 비추천은 없음
         */

        //given
        member = createAndSaveMember("myEmail@google.com", memberPassword);
        otherMember = createAndSaveMember("otherEmail@google.com", otherMemberPassword);

        Tag tag1 = createAndSaveTag("tag1");
        Tag tag2 = createAndSaveTag("tag2");

        tags = List.of(tag1, tag2);

        for (int i = 0; i < 20; i++) {
            Question myQuestion = createAndSaveQuestion(member);
            useTagInQuestion(myQuestion, tag1);
            useTagInQuestion(myQuestion, tag2);
            questions.add(myQuestion);

            Question otherQuestion = createAndSaveQuestion(otherMember);
            useTagInQuestion(otherQuestion, tag1);
            useTagInQuestion(otherQuestion, tag2);
            questions.add(otherQuestion);

            Answer answer = createAndSaveAnswer(member, otherQuestion);
            answers.add(answer);
            for (int j = 0; j < 10; j++) {
                Reply myReply = createAndSaveReply(member, answer);
                replies.add(myReply);
            }
        }

        memberRepository.flush();

        //accessToken 발급을 위한 로그인
        String request = objectMapper.writeValueAsString(new LoginDto(member.getEmail(), "1q2w3e4r!"));

        ResultActions actions = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(request));

        accessToken = actions.andReturn().getResponse().getHeader("Authorization");

        //otherMember 의 accessToken 발급을 위한 로그인
        String otherMemberRequest = objectMapper.writeValueAsString(new LoginDto(otherMember.getEmail(), "1q2w3e4r!"));

        ResultActions otherMemberActions = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(otherMemberRequest));

        otherAccessToken = otherMemberActions.andReturn().getResponse().getHeader("Authorization");
    }

    @AfterAll
    void afterAll() {
        deleteAll();
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void getMember() throws Exception {
        //given
        Long memberId = member.getMemberId();

        //when
        ResultActions actions = mockMvc.perform(get("/members/{member-id}", memberId)
                .accept(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        MemberResponse memberResponse = getApiSingleResponseFromResult(actions, MemberResponse.class).getData();
        //멤버 정보 확인
        assertThat(memberResponse.getMemberId()).isEqualTo(memberId);
        assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberResponse.getAuthority()).isEqualTo(member.getAuthority());

        //멤버 myInfo 확인
        assertThat(memberResponse.getMyIntro()).isEqualTo(member.getMyInfo().getMyIntro());
        assertThat(memberResponse.getTitle()).isEqualTo(member.getMyInfo().getTitle());
        assertThat(memberResponse.getLocation()).isEqualTo(member.getMyInfo().getLocation());
        assertThat(memberResponse.getAccounts()).isEmpty();
        assertThat(memberResponse.getImage()).isNull();

        //멤버 질문 확인
        assertThat(memberResponse.getQuestion().getQuestions()).hasSize(5);
        assertThat(memberResponse.getQuestion().getPageInfo().getPage()).isEqualTo(1);
        assertThat(memberResponse.getQuestion().getPageInfo().getSize()).isEqualTo(5);
        assertThat(memberResponse.getQuestion().getPageInfo().isFirst()).isTrue();

        //멤버 답변 확인
        assertThat(memberResponse.getAnswer().getAnswers()).hasSize(5);
        assertThat(memberResponse.getAnswer().getPageInfo().getPage()).isEqualTo(1);
        assertThat(memberResponse.getAnswer().getPageInfo().getSize()).isEqualTo(5);
        assertThat(memberResponse.getAnswer().getPageInfo().isFirst()).isTrue();

        //멤버 태그 확인
        assertThat(memberResponse.getTags()).hasSize(2)
                .extracting("tagName")
                .containsExactlyInAnyOrder("tag1", "tag2");
    }

    @Test
    @DisplayName("회원 정보 중 Question 정보 2페이지 조회")
    void getMemberQuestion() throws Exception {
        //given
        Long memberId = member.getMemberId();

        //when
        ResultActions actions = mockMvc.perform(get("/members/{member-id}/questions", memberId)
                        .param("page", "2")
                        .accept(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        List<MemberResponse> memberResponseList = getApiPageResponseFromResult(actions, MemberResponse.class).getData();
        PageInfo pageInfo = getApiPageResponseFromResult(actions, MemberResponse.class).getPageInfo();

        assertThat(memberResponseList).hasSize(5);
        assertThat(pageInfo.getPage()).isEqualTo(2);
        assertThat(pageInfo.getSize()).isEqualTo(5);
        assertThat(pageInfo.isFirst()).isFalse();
    }

    @Test
    @DisplayName("회원 정보 중 Answer 정보 2페이지 조회")
    void getMemberAnswer() throws Exception {
        //given
        Long memberId = member.getMemberId();

        //when
        ResultActions actions = mockMvc.perform(get("/members/{member-id}/answers", memberId)
                .param("page", "2")
                .accept(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        List<AnswerResponse> answerResponseList = getApiPageResponseFromResult(actions, AnswerResponse.class).getData();
        PageInfo pageInfo = getApiPageResponseFromResult(actions, MemberResponse.class).getPageInfo();

        assertThat(answerResponseList).hasSize(5);
        assertThat(pageInfo.getPage()).isEqualTo(2);
        assertThat(pageInfo.getSize()).isEqualTo(5);
        assertThat(pageInfo.isFirst()).isFalse();
    }

    @TestFactory
    @DisplayName("회원 정보 수정 성공 테스트")
    Collection<DynamicTest> memberUpdate() {
        //given
        Long memberId = member.getMemberId();
        String updateNickName = "newNickName";
        String updateTitle = "newTitle";
        String updateLocation = "newLocation";
        String updateMyIntro = "newMyIntro";
        List<String> updateAccounts = List.of("newAccount1", "newAccount2");

        return List.of(
                dynamicTest("nickName 을 수정할 수 있다.", () -> {
                    //given
                    MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                            .nickname(updateNickName)
                            .build();

                    String content = objectMapper.writeValueAsString(request);

                    //when
                    ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON)
                            .content(content));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    Member updatedMember = memberRepository.findById(memberId).orElseThrow();

                    assertThat(updatedMember.getNickname()).isEqualTo(updateNickName);
                }),
                dynamicTest("myInfo 의 title 을 수정할 수 있다.", () -> {
                    //given
                    MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                            .title(updateTitle)
                            .build();

                    String content = objectMapper.writeValueAsString(request);

                    //when
                    ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON)
                            .content(content));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    Member updatedMember = memberRepository.findByMemberIdWithInfo(memberId).orElseThrow();

                    assertThat(updatedMember.getMyInfo().getTitle()).isEqualTo(updateTitle);
                }),
                dynamicTest("myInfo 의 myIntro 를 수정할 수 있다.", () -> {
                    //given
                    MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                            .myIntro(updateMyIntro)
                            .build();

                    String content = objectMapper.writeValueAsString(request);

                    //when
                    ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON)
                            .content(content));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    Member updatedMember = memberRepository.findByMemberIdWithInfo(memberId).orElseThrow();

                    assertThat(updatedMember.getMyInfo().getMyIntro()).isEqualTo(updateMyIntro);
                }),
                dynamicTest("myInfo 의 location 를 수정할 수 있다.", () -> {
                    //given
                    MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                            .location(updateLocation)
                            .build();

                    String content = objectMapper.writeValueAsString(request);

                    //when
                    ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON)
                            .content(content));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    Member updatedMember = memberRepository.findByMemberIdWithInfo(memberId).orElseThrow();

                    assertThat(updatedMember.getMyInfo().getLocation()).isEqualTo(updateLocation);
                }),
                dynamicTest("myInfo 의 accounts 를 수정할 수 있다.", () -> {
                    //given
                    MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                            .accounts(updateAccounts)
                            .build();

                    String content = objectMapper.writeValueAsString(request);

                    //when
                    ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON)
                            .content(content));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    Member updatedMember = memberRepository.findByMemberIdWithInfo(memberId).orElseThrow();

                    assertThat(updatedMember.getMyInfo().getAccounts()).contains(updateAccounts.get(0), updateAccounts.get(1));
                }),
                dynamicTest("myInfo 를 다시 조회하면 수정한 값을 다 확인할 수 있다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/members/{member-id}", memberId)
                            .accept(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data.nickname").value(updateNickName))
                            .andExpect(jsonPath("$.data.title").value(updateTitle))
                            .andExpect(jsonPath("$.data.myIntro").value(updateMyIntro))
                            .andExpect(jsonPath("$.data.location").value(updateLocation))
                            .andExpect(jsonPath("$.data.accounts[0]").value(updateAccounts.get(0)))
                            .andExpect(jsonPath("$.data.accounts[1]").value(updateAccounts.get(1)));
                })
        );
    }

    @Test
    @DisplayName("다른 회원 정보 수정 테스트 -> 403 예외 반환")
    void memberUpdateOtherMember() throws Exception {
        //given
        Long memberId = member.getMemberId();
        String updateNickName = "newNickName";
        String updateTitle = "newTitle";
        String updateLocation = "newLocation";
        String updateMyIntro = "newMyIntro";
        List<String> updateAccounts = List.of("newAccount1", "newAccount2");


        //given
        MemberUpdateApiRequest request = MemberUpdateApiRequest.builder()
                .nickname(updateNickName)
                .title(updateTitle)
                .location(updateLocation)
                .myIntro(updateMyIntro)
                .accounts(updateAccounts)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(patch("/members/{member-id}", memberId)
                .header("Authorization", otherAccessToken) // 다른 회원의 토큰
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        actions
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("비밀번호 변경 성공 테스트")
    void changePassword() throws Exception {
        //given
        Long memberId = member.getMemberId();
        String newPassword = "newPassword12!!";

        MemberPasswordUpdateAPiRequest request = MemberPasswordUpdateAPiRequest.builder()
                .password(memberPassword)
                .newPassword(newPassword)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(patch("/members/{member-id}/password", memberId)
                .header("Authorization", accessToken)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        Member updatedMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("비밀번호 변경 실패 테스트 - 이전 비밀번호가 맞지 않을 때")
    void changePasswordNotCollectPrevPW() throws Exception {
        //given
        Long memberId = member.getMemberId();
        String newPassword = "newPassword12!!";

        MemberPasswordUpdateAPiRequest request = MemberPasswordUpdateAPiRequest.builder()
                .password(memberPassword + "a") // 이전 비밀번호가 맞지 않음
                .newPassword(newPassword)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(patch("/members/{member-id}/password", memberId)
                .header("Authorization", accessToken)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest());

        //비밀번호는 변경되지 않는다.
        Member updatedMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(passwordEncoder.matches(memberPassword, updatedMember.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isFalse();
    }

    @Test
    @DisplayName("비밀번호 변경 실패 테스트 - 새 비밀번호가 유효하지 않을 때")
    void changePasswordNotValid() throws Exception {
        //given
        Long memberId = member.getMemberId();
        String newPassword = "new!!"; // 유효하지 않은 비밀번호

        MemberPasswordUpdateAPiRequest request = MemberPasswordUpdateAPiRequest.builder()
                .password(memberPassword)
                .newPassword(newPassword)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(patch("/members/{member-id}/password", memberId)
                .header("Authorization", accessToken)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest());

        //비밀번호는 변경되지 않는다.
        Member updatedMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(passwordEncoder.matches(memberPassword, updatedMember.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isFalse();
    }

    @Test
    @DisplayName("비밀번호 변경 실패 테스트 - 다른 회원의 비밀번호 변경 시도 시")
    void changePasswordAccessDenied() throws Exception {
        //given
        Long memberId = member.getMemberId();
        String newPassword = "newPassword12!!";

        MemberPasswordUpdateAPiRequest request = MemberPasswordUpdateAPiRequest.builder()
                .password(memberPassword)
                .newPassword(newPassword)
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(patch("/members/{member-id}/password", memberId)
                .header("Authorization", otherAccessToken) // 다른 회원의 토큰 (다른 회원이 로그인한 상태)
                .contentType(APPLICATION_JSON)
                .content(content));

        //then
        actions
                .andDo(print())
                .andExpect(status().isForbidden());

        //비밀번호는 변경되지 않는다.
        Member updatedMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(passwordEncoder.matches(memberPassword, updatedMember.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isFalse();
    }

    @Test
    @DisplayName("멤버 탈퇴 성공 테스트")
    void deleteMember() throws Exception {
        //given
        Long memberId = member.getMemberId();

        ///when
        ResultActions actions = mockMvc.perform(delete("/members/{member-id}", memberId)
                .header("Authorization", accessToken)
                .contentType(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //멤버가 삭제되었는지 확인
        Member deleteMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(deleteMember.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("멤버 탈퇴 실패 테스트 - 다른 회원의 id 값으로 탈퇴 시도 시 403 에러 반환")
    void deleteMemberAccessDenied() throws Exception {
        //given
        Long memberId = member.getMemberId();

        ///when
        ResultActions actions = mockMvc.perform(delete("/members/{member-id}", memberId)
                .header("Authorization", otherAccessToken) // 다른 회원의 토큰
                .contentType(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isForbidden());

        //멤버가 삭제되지 않았는지 확인
        Member deleteMember = memberRepository.findById(memberId).orElseThrow();
        assertThat(deleteMember.isEnabled()).isTrue();
    }

    @TestFactory
    @DisplayName("S3 버킷에 이미지 저장, 삭제 테스트")
    Collection<DynamicTest> imageTest() {
        //given
        Long memberId = member.getMemberId();

        return List.of(
                dynamicTest("member 에 이미지를 업데이트 한다.", () -> {
                    //given
                    MockMultipartFile file =
                            new MockMultipartFile(
                                    "file",
                                    "test.png",
                                    IMAGE_PNG_VALUE,
                                    "test".getBytes());


                    //when
                    ResultActions actions = mockMvc.perform(
                            multipart("http://localhost:8080/members/{member-id}/image", memberId)
                                    .file(file)
                                    .header("Authorization", accessToken)
                                    .contentType(MULTIPART_FORM_DATA)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    }));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent())
                            .andExpect(header().exists("Location"));

                    //member 정보에 이미지가 업데이트 되었는지 확인
                    Member updatedMember = memberRepository.findById(memberId).orElseThrow();
                    assertThat(updatedMember.getMyInfo().getImage()).isNotNull();

                    //Location 에 있는 presignedUrl 로 접근할 수 있는지 학인
                    String preSignedUrl = actions.andReturn().getResponse().getHeader("Location");
                    ResponseEntity<byte[]> response = getResponseEntity(preSignedUrl);
                    assertThat(response.getStatusCodeValue()).isEqualTo(200);
                }),
                dynamicTest("member 정보를 받으면 image 값으로 presignedUrl 을 받는다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/members/{member-id}", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk());

                    //presignedUrl 로 접근할 수 있는지 확인
                    MemberResponse memberResponse = getApiSingleResponseFromResult(actions, MemberResponse.class).getData();
                    String preSignedUrl = memberResponse.getImage();
                    ResponseEntity<byte[]> response = getResponseEntity(preSignedUrl);
                    assertThat(response.getStatusCodeValue()).isEqualTo(200);
                }),
                dynamicTest("member 의 이미지를 삭제한다.", () -> {
                    //given
                    Member member = memberRepository.findById(memberId).orElseThrow();
                    String preSignedUrl = s3Service.getPreSignedUrl(member.getMyInfo().getImage()); //미리 presignedUrl 을 받아둔다.

                    //when
                    ResultActions actions = mockMvc.perform(delete("/members/{member-id}/image", memberId)
                            .header("Authorization", accessToken)
                            .contentType(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());

                    //미리 받아둔 presignedUrl 로 접근할 수 있는지 확인 (404 에러)
                    assertThatThrownBy(
                            () -> getResponseEntity(preSignedUrl))
                            .isInstanceOf(HttpClientErrorException.NotFound.class);

                    //member 의 image 값이 null 인지 확인
                    Member updatedMember = memberRepository.findById(memberId).orElseThrow();
                    assertThat(updatedMember.getMyInfo().getImage()).isNull();
                })
        );
    }

    private ResponseEntity<byte[]> getResponseEntity(String url) throws UnsupportedEncodingException {
        return restTemplate.getForEntity(
                URLDecoder.decode(url, "UTF-8"),
                byte[].class);
    }
}

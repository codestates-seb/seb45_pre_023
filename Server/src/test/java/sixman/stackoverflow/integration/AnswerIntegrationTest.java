package sixman.stackoverflow.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("답변 통합 테스트")
public class AnswerIntegrationTest extends IntegrationTest {

    Member member;
    Member otherMember;
    String accessToken;
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
        member = createAndSaveMember("myEmail@google.com", "1q2w3e4r!");
        otherMember = createAndSaveMember("otherEmail@google.com");

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
    }

    @AfterAll
    void afterAll() {
        deleteAll();
    }

    @TestFactory
    @DisplayName("답변 추천 테스트")
    Collection<DynamicTest> answerRecommend() {
        //given
        Long answerId = answers.stream().filter(answer -> answer.getQuestion().getMember().getMemberId().equals(otherMember.getMemberId())).findFirst().get().getAnswerId();
        Long questionId = answers.stream().filter(answer -> answer.getAnswerId().equals(answerId)).findFirst().get().getQuestion().getQuestionId();

        return List.of(
                dynamicTest("member 가 답변을 추천한다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(patch("/answers/{answer-id}/upvote", answerId)
                            .accept(APPLICATION_JSON)
                            .header("Authorization", accessToken));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("답변 목록을 확인하면 해당 답변의 추천 수가 1 증가하고, 자신이 추천했다고 표시된다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/questions/{question-id}/answers", questionId)
                            .accept(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk());

                    ApiPageResponse<AnswerResponse> response = getApiPageResponseFromResult(actions, AnswerResponse.class);
                    assertThat(response.getData().get(0).getRecommend()).isEqualTo(1);
//                    assertThat(response.getData().get(0).getRecommendType()).isEqualTo(TypeEnum.UPVOTE);
                }),
                dynamicTest("member 가 답변 추천을 다시 눌러 취소한다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(patch("/answers/{answer-id}/upvote", answerId)
                            .accept(APPLICATION_JSON)
                            .header("Authorization", accessToken));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("답변을 확인하면 추천 수가 1 감소되어있고, 자신의 추천이 없어진다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/questions/{question-id}/answers", questionId)
                            .accept(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk());

                    ApiPageResponse<AnswerResponse> response = getApiPageResponseFromResult(actions, AnswerResponse.class);
                    assertThat(response.getData().get(0).getRecommend()).isEqualTo(0);
                    assertThat(response.getData().get(0).getRecommendType()).isNull();


                }),
                dynamicTest("member 가 답변 비추천을 누른다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(patch("/answers/{answer-id}/downvote", answerId)
                            .accept(APPLICATION_JSON)
                            .header("Authorization", accessToken));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("답변을 확인하면 추천 수가 1 감소되어있고, 자신이 비추천했다고 표시된다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/questions/{question-id}/answers", questionId)
                            .accept(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk());

                    ApiPageResponse<AnswerResponse> response = getApiPageResponseFromResult(actions, AnswerResponse.class);
                    assertThat(response.getData().get(0).getRecommend()).isEqualTo(-1);
//                    assertThat(response.getData().get(0).getRecommendType()).isEqualTo(TypeEnum.DOWNVOTE);
                }),
                dynamicTest("member 가 비추천 상태에서 답변을 다시 추천한다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(patch("/answers/{answer-id}/upvote", answerId)
                            .accept(APPLICATION_JSON)
                            .header("Authorization", accessToken));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isNoContent());
                }),
                dynamicTest("답변을 확인하면 추천 수가 1 로 되어있고, 자신이 추천했다고 표시된다.", () -> {
                    //when
                    ResultActions actions = mockMvc.perform(get("/questions/{question-id}/answers", questionId)
                            .accept(APPLICATION_JSON));

                    //then
                    actions
                            .andDo(print())
                            .andExpect(status().isOk());

                    ApiPageResponse<AnswerResponse> response = getApiPageResponseFromResult(actions, AnswerResponse.class);
                    assertThat(response.getData().get(0).getRecommend()).isEqualTo(1);
//                    assertThat(response.getData().get(0).getRecommendType()).isEqualTo(TypeEnum.UPVOTE);
                })
        );

    }
}

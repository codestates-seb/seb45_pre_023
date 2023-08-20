package sixman.stackoverflow.integration;

import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("회원 조회, 수정, 삭제 통합 테스트")
public class MemberIntegrationTest extends IntegrationTest{

    Member member;
    Member otherMember;

    @BeforeAll
    void beforeAll() {
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

        for (int i = 0; i < 20; i++) {
            Question myQuestion = createAndSaveQuestion(member);
            useTagInQuestion(myQuestion, tag1);
            useTagInQuestion(myQuestion, tag2);

            Question otherQuestion = createAndSaveQuestion(otherMember);
            useTagInQuestion(otherQuestion, tag1);
            useTagInQuestion(otherQuestion, tag2);
            Answer answer = createAndSaveAnswer(member, otherQuestion);
            for (int j = 0; j < 10; j++) {
                createAndSaveReply(member, answer);
            }
        }
    }

    @AfterAll
    void afterAll() {
        questionTagRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        replyRepository.deleteAllInBatch();
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
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
        ApiSingleResponse<MemberResponse> memberResponse = getApiSingleResponseFromResult(actions, MemberResponse.class);

    }
}

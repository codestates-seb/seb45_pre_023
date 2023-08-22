package sixman.stackoverflow.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("태그 통합 테스트")
public class TagIntegrationTest extends IntegrationTest{

    List<Tag> tags = new ArrayList<>();

    @BeforeAll
    void beforeAll() {
        /**
         * 최초 데이터
         * 1. member 1명
         * 2. 태그 3개
         */
        //given
        Tag tag1 = createAndSaveTag("tag1");
        Tag tag2 = createAndSaveTag("tag2");
        Tag tag3 = createAndSaveTag("tag3");

        tags = List.of(tag1, tag2, tag3);
    }

    @AfterAll
    void afterAll() {
        deleteAll();
    }

    @Test
    @DisplayName("태그 목록 조회 테스트")
    void getTags() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(get("/tags")
                .accept(APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        List response = getApiSingleResponseFromResult(actions, List.class).getData();

        assertThat(response).hasSize(3)
                .extracting("tagName")
                .containsExactlyInAnyOrder("tag1", "tag2", "tag3");


    }
}

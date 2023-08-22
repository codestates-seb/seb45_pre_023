package sixman.stackoverflow.domain.tag.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerTest extends ControllerTest {

    @Test
    @DisplayName("태그 조회 API")
    void getTags() throws Exception {
        //given
        List<QuestionTagResponse> responses = creatQuestionTagResponses(3);

        given(tagService.getTags()).willReturn(responses);

        String result = objectMapper.writeValueAsString(ApiSingleResponse.ok(responses));

        //when
        ResultActions actions = mockMvc.perform(
                get("/tags")
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        //rest docs
        actions.andDo(documentHandler.document(
                responseFields(
                        fieldWithPath("data[].tagId").description("태그 ID"),
                        fieldWithPath("data[].tagName").description("태그 이름"),
                        fieldWithPath("data[].tagDetail").description("태그 설명"),
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("status").description("응답 상태"),
                        fieldWithPath("message").description("응답 메시지")
                )
        ));
    }

    private List<QuestionTagResponse> creatQuestionTagResponses(int count) {
        List<QuestionTagResponse> reponse = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            QuestionTagResponse questionTagResponse = QuestionTagResponse.builder()
                    .tagId((long) i)
                    .tagName("tag" + i)
                    .tagDetail("tag" + i + " detail")
                    .build();

            reponse.add(questionTagResponse);
        }
        return reponse;
    }
}
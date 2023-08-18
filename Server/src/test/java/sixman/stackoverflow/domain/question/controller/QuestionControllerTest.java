package sixman.stackoverflow.domain.question.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionSortRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionUpdateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import javax.persistence.EnumType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class QuestionControllerTest extends ControllerTest {

    @Test
    @DisplayName("질문 생성 API")
    void createQuestion() throws Exception {
        //given
        Long createdQuestionId = 1L;

        QuestionCreateApiRequest request = QuestionCreateApiRequest.builder()
                .title("title")
                .detail("detail")
                .expect("expect")
                .tagIds(Arrays.asList(1, 2))
                .build();

        setDefaultAuthentication(1L);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(questionService.createQuestion(any(Question.class), any(List.class))).willReturn(createdQuestionId);

        //when
        ResultActions actions = mockMvc.perform(
                post("/questions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/questions/" + createdQuestionId));

        //restDocs
        setConstraintClass(QuestionCreateApiRequest.class);

        actions.andDo(documentHandler.document(
                requestHeaders(
                        headerWithName("Authorization").description("AccessToken")
                ),
                requestFields(
                        fieldWithPath("title").description("질문 제목").attributes(getConstraint("title")),
                        fieldWithPath("detail").description("질문 내용").attributes(getConstraint("detail")),
                        fieldWithPath("expect").description("질문 내용2").attributes(getConstraint("expect")),
                        fieldWithPath("tagIds").description("질문 태그").attributes(getConstraint("tagIds"))
                ),
                responseHeaders(
                        headerWithName("Location").description("생성된 질문의 URI")
                )
        ));
    }

    @Test
    @DisplayName("질문 목록 조회 API")
    void getQuestions() throws Exception {
        //given
        Integer page = 1;

        QuestionTagResponse tag1 = QuestionTagResponse.builder()
                .tagId(1L)
                .tagName("tag1")
                .build();

        QuestionTagResponse tag2 = QuestionTagResponse.builder()
                .tagId(2L)
                .tagName("tag2")
                .build();

        List<QuestionResponse> responses = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            QuestionResponse response = QuestionResponse.builder()
                    .questionId((long) i)
                    .title("title")
                    .detail("detail")
                    .answerCount(5)
                    .member(MemberInfo.of(createMember(1L)))
                    .views(100)
                    .recommend(10)
                    .tags(List.of(tag1, tag2))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            responses.add(response);
        }

        Page<QuestionResponse> questionResponses = new PageImpl<>(responses, PageRequest.of(page - 1, 10), 100);

        given(questionService.getLatestQuestions(any(Pageable.class))).willReturn(questionResponses);

        String result = objectMapper.writeValueAsString(ApiPageResponse.ok(questionResponses, "질문 목록 조회 성공"));

        //when
        ResultActions actions = mockMvc.perform(
                get("/questions")
                        .accept(APPLICATION_JSON)
                        .param("page", String.valueOf(page))
                        .param("sort", "CREATED_DATE")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        //restDocs
        actions.andDo(
                documentHandler.document(
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("sort").description(generateLinkCode(QuestionSortRequest.class))
                        ),
                        responseFields(
                                fieldWithPath("data").description("질문 목록"),
                                fieldWithPath("data[].questionId").description("질문 ID"),
                                fieldWithPath("data[].title").description("질문 제목"),
                                fieldWithPath("data[].detail").description("질문 내용"),
                                fieldWithPath("data[].answerCount").description("질문의 답변 개수"),
                                fieldWithPath("data[].member").description("질문 작성자 정보"),
                                fieldWithPath("data[].member.memberId").description("질문 작성자 ID"),
                                fieldWithPath("data[].member.nickname").description("질문 작성자 닉네임"),
                                fieldWithPath("data[].member.imageUrl").description("질문 작성자 이미지 URL"),
                                fieldWithPath("data[].views").description("조회수"),
                                fieldWithPath("data[].recommend").description("추천수"),
                                fieldWithPath("data[].tags").description("질문 태그 정보"),
                                fieldWithPath("data[].tags[].tagId").description("질문 태그 ID"),
                                fieldWithPath("data[].tags[].tagName").description("질문 태그 이름"),
                                fieldWithPath("data[].createdDate").description("질문 생성일"),
                                fieldWithPath("data[].updatedDate").description("질문 수정일"),
                                fieldWithPath("pageInfo").description("질문 페이징 정보"),
                                fieldWithPath("pageInfo.page").description("질문 현재 페이지"),
                                fieldWithPath("pageInfo.size").description("질문 페이지 사이즈"),
                                fieldWithPath("pageInfo.totalPage").description("질문 전체 페이지 수"),
                                fieldWithPath("pageInfo.totalSize").description("질문 전체 개수"),
                                fieldWithPath("pageInfo.first").description("질문 첫 페이지 여부"),
                                fieldWithPath("pageInfo.last").description("질문 마지막 페이지 여부"),
                                fieldWithPath("pageInfo.hasNext").description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.hasPrevious").description("이전 페이지가 있는지"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")

                        )
                ));
    }

    @Test
    @DisplayName("질문 단건 조회 API")
    void getQuestionById() throws Exception {
        //given
        QuestionTagResponse tag1 = QuestionTagResponse.builder()
                .tagId(1L)
                .tagName("tag1")
                .build();

        QuestionTagResponse tag2 = QuestionTagResponse.builder()
                .tagId(2L)
                .tagName("tag2")
                .build();

        QuestionDetailResponse response = QuestionDetailResponse.builder()
                .questionId(1L)
                .title("title")
                .detail("detail")
                .expect("expect")
                .member(MemberInfo.of(createMember(1L)))
                .views(100)
                .recommend(10)
                .recommendType(TypeEnum.UPVOTE)
                .tags(List.of(tag1, tag2))
                .answer(createAnswerResponse())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        given(questionService.getQuestionById(anyLong())).willReturn(response);

        String result = objectMapper.writeValueAsString(ApiSingleResponse.ok(response, "질문 조회 성공"));


        //when
        ResultActions actions = mockMvc.perform(
                get("/questions/{question-id}", 1L)
                        .accept(APPLICATION_JSON)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("question-id").description("질문 ID")
                        ),
                        responseFields(
                                fieldWithPath("data").description("질문 정보"),
                                fieldWithPath("data.questionId").description("질문 ID"),
                                fieldWithPath("data.title").description("질문 제목"),
                                fieldWithPath("data.detail").description("질문 내용"),
                                fieldWithPath("data.expect").description("질문 내용2"),
                                fieldWithPath("data.member").description("질문 작성자 정보"),
                                fieldWithPath("data.member.memberId").description("질문 작성자 ID"),
                                fieldWithPath("data.member.nickname").description("질문 작성자 닉네임"),
                                fieldWithPath("data.member.imageUrl").description("질문 작성자 이미지 URL"),
                                fieldWithPath("data.views").description("조회수"),
                                fieldWithPath("data.recommend").description("추천수"),
                                fieldWithPath("data.recommendType").description(generateLinkCode(EnumType.class)),
                                fieldWithPath("data.tags").description("질문 태그 정보"),
                                fieldWithPath("data.tags[].tagId").description("질문 태그 ID"),
                                fieldWithPath("data.tags[].tagName").description("질문 태그 이름"),
                                fieldWithPath("data.createdDate").description("질문 생성일"),
                                fieldWithPath("data.updatedDate").description("질문 수정일"),
                                fieldWithPath("data.answer").description("질문 답변 정보"),
                                fieldWithPath("data.answer.answers[]").description("질문 답변 목록"),
                                fieldWithPath("data.answer.answers[].answerId").description("질문 답변 ID"),
                                fieldWithPath("data.answer.answers[].content").description("질문 답변 내용"),
                                fieldWithPath("data.answer.answers[].member").description("질문 답변 작성자 정보"),
                                fieldWithPath("data.answer.answers[].member.memberId").description("질문 답변 작성자 ID"),
                                fieldWithPath("data.answer.answers[].member.nickname").description("질문 답변 작성자 닉네임"),
                                fieldWithPath("data.answer.answers[].member.imageUrl").description("질문 답변 작성자 이미지 URL"),
                                fieldWithPath("data.answer.answers[].recommend").description("질문 답변 추천수"),
                                fieldWithPath("data.answer.answers[].recommendType").description(generateLinkCode(EnumType.class)),
                                fieldWithPath("data.answer.answers[].createdDate").description("질문 답변 생성일"),
                                fieldWithPath("data.answer.answers[].updatedDate").description("질문 답변 수정일"),
                                fieldWithPath("data.answer.pageInfo").description("질문 답변 페이징 정보"),
                                fieldWithPath("data.answer.pageInfo.page").description("질문 답변 현재 페이지"),
                                fieldWithPath("data.answer.pageInfo.size").description("질문 답변 페이지 사이즈"),
                                fieldWithPath("data.answer.pageInfo.totalPage").description("질문 답변 전체 페이지 수"),
                                fieldWithPath("data.answer.pageInfo.totalSize").description("질문 답변 전체 개수"),
                                fieldWithPath("data.answer.pageInfo.first").description("질문 답변 첫 페이지 여부"),
                                fieldWithPath("data.answer.pageInfo.last").description("질문 답변 마지막 페이지 여부"),
                                fieldWithPath("data.answer.pageInfo.hasNext").description("질문 답변 다음 페이지 여부"),
                                fieldWithPath("data.answer.pageInfo.hasPrevious").description("질문 답변 이전 페이지 여부"),
                                fieldWithPath("data.answer.answers[].reply").description("질문 답변의 댓글 정보"),
                                fieldWithPath("data.answer.answers[].reply.replies[]").description("질문 답변의 댓글 목록"),
                                fieldWithPath("data.answer.answers[].reply.replies[].replyId").description("질문 답변의 댓글 ID"),
                                fieldWithPath("data.answer.answers[].reply.replies[].content").description("질문 답변의 댓글 내용"),
                                fieldWithPath("data.answer.answers[].reply.replies[].member").description("질문 답변의 댓글 작성자 정보"),
                                fieldWithPath("data.answer.answers[].reply.replies[].member.memberId").description("질문 답변의 댓글 작성자 ID"),
                                fieldWithPath("data.answer.answers[].reply.replies[].member.nickname").description("질문 답변의 댓글 작성자 닉네임"),
                                fieldWithPath("data.answer.answers[].reply.replies[].member.imageUrl").description("질문 답변의 댓글 작성자 이미지 URL"),
                                fieldWithPath("data.answer.answers[].reply.replies[].createdDate").description("질문 답변의 댓글 생성일"),
                                fieldWithPath("data.answer.answers[].reply.replies[].updatedDate").description("질문 답변의 댓글 수정일"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo").description("질문 답변의 댓글 페이징 정보"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.page").description("질문 답변의 댓글 현재 페이지"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.size").description("질문 답변의 댓글 페이지 사이즈"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.totalPage").description("질문 답변의 댓글 전체 페이지 수"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.totalSize").description("질문 답변의 댓글 전체 개수"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.first").description("질문 답변의 댓글 첫 페이지 여부"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.last").description("질문 답변의 댓글 마지막 페이지 여부"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.hasNext").description("질문 답변의 댓글 다음 페이지 여부"),
                                fieldWithPath("data.answer.answers[].reply.pageInfo.hasPrevious").description("질문 답변의 댓글 이전 페이지 여부"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                )
        );
    }

    @Test
    @DisplayName("질문 수정 API")
    void updateQuestion() throws Exception {
        //given
        QuestionUpdateApiRequest request = QuestionUpdateApiRequest.builder()
                .title("update title")
                .detail("update detail")
                .expect("update expect")
                .tagIds(Arrays.asList(1, 2, 3))
                .build();

        Long questionId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                patch("/questions/{question-id}", questionId)
                        .header("Authorization", "Bearer abc.def.ghi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        setConstraintClass(QuestionUpdateApiRequest.class);

        actions.andDo(
                documentHandler.document(
                        requestHeaders(
                                headerWithName("Authorization").description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("question-id").description("수정할 질문 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("질문 제목").attributes(getConstraint("title")),
                                fieldWithPath("detail").description("질문 내용").attributes(getConstraint("detail")),
                                fieldWithPath("expect").description("질문 내용2").attributes(getConstraint("expect")),
                                fieldWithPath("tagIds").description("질문 태그 ID 목록").attributes(getConstraint("tagIds"))
                        )
                )
        );
    }

    @Test
    @DisplayName("질문글 추천 API")
    void recommendQuestion() throws Exception {
        //given
        Long questionId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                patch("/questions/{question-id}/upvote", questionId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        requestHeaders(
                                headerWithName("Authorization").description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("question-id").description("추천할 질문 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("질문글 비추천 API")
    void disrecommendQuestion() throws Exception {
        //given
        Long questionId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                patch("/questions/{question-id}/downvote", questionId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        requestHeaders(
                                headerWithName("Authorization").description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("question-id").description("비추천할 질문 ID")
                        )
                )
        );
    }

    @Test
    @DisplayName("태그 수정 API")
    void updateTags() {
        //given
        Long questionId = 1L;
        List<String> tagNames = Arrays.asList("tag1", "tag2", "tag3");

        //when


        //then

    }

    @Test
    @DisplayName("질문 삭제 API")
    void deleteQuestion() throws Exception {
        //given
        Long questionId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                delete("/questions/{question-id}", questionId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        requestHeaders(
                                headerWithName("Authorization").description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("question-id").description("삭제할 질문 ID")
                        )
                )
        );
    }
}

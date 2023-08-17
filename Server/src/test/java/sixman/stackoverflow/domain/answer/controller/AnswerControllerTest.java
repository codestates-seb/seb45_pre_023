package sixman.stackoverflow.domain.answer.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerUpdateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.controller.dto.AnswerSortRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.testhelper.ControllerTest;

import javax.persistence.EnumType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnswerControllerTest extends ControllerTest {

    @Test
    @DisplayName("답변 생성 API")
    void createAnswer() throws Exception {
        //given
        Long questionId = 1L;
        Long createdAnswerId = 1L;

        AnswerCreateApiRequest request = AnswerCreateApiRequest.builder()
                .content("답변 내용")
                .build();

        given(answerService.createAnswer(any(AnswerCreateServiceRequest.class), anyLong())).willReturn(createdAnswerId);

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(
                post("/questions/{question-id}/answers", questionId)
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/answers/" + createdAnswerId));

        //restDocs
        setConstraintClass(AnswerCreateApiRequest.class);

        actions
                .andDo(documentHandler.document(
                        pathParameters(
                                parameterWithName("question-id").description("질문 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("content").description("답변 내용").attributes(getConstraint("content"))
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성된 답변의 URI")
                        )
                ));


    }
    @Test
    @DisplayName("답변 목록 조회 API")
    void getAnswers() throws Exception {
        //given
        Long questionId = 1L;
        Long page = 1L;

        List<AnswerResponse> answers = createAnswers();
        PageRequest pageRequest = PageRequest.of(page.intValue() - 1, 5);

        Page<AnswerResponse> answerResponses = new PageImpl<>(answers, pageRequest, 20);

        given(answerService.findAnswers(anyLong(), any(PageRequest.class))).willReturn(answerResponses);

        String result = objectMapper.writeValueAsString(ApiPageResponse.ok(answerResponses));

        //when
        ResultActions actions = mockMvc.perform(
                get("/questions/{question-id}/answers", questionId)
                        .accept(APPLICATION_JSON)
                        .param("page", page.toString())
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
                        pathParameters(
                                parameterWithName("question-id").description("질문 ID")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("sort").description(generateLinkCode(AnswerSortRequest.class))
                        ),
                        responseFields(
                                fieldWithPath("data").description("질문 답변 정보"),
                                fieldWithPath("data[].answerId").description("질문 답변 ID"),
                                fieldWithPath("data[].content").description("질문 답변 내용"),
                                fieldWithPath("data[].member").description("질문 답변 작성자 정보"),
                                fieldWithPath("data[].member.memberId").description("질문 답변 작성자 ID"),
                                fieldWithPath("data[].member.nickname").description("질문 답변 작성자 닉네임"),
                                fieldWithPath("data[].member.imageUrl").description("질문 답변 작성자 이미지 URL"),
                                fieldWithPath("data[].recommend").description("질문 답변 추천수"),
                                fieldWithPath("data[].recommendType").description(generateLinkCode(EnumType.class)),
                                fieldWithPath("data[].createdDate").description("질문 답변 생성일"),
                                fieldWithPath("data[].updatedDate").description("질문 답변 수정일"),
                                fieldWithPath("pageInfo").description("질문 답변 페이징 정보"),
                                fieldWithPath("pageInfo.page").description("질문 답변 현재 페이지"),
                                fieldWithPath("pageInfo.size").description("질문 답변 페이지 사이즈"),
                                fieldWithPath("pageInfo.totalPage").description("질문 답변 전체 페이지 수"),
                                fieldWithPath("pageInfo.totalSize").description("질문 답변 전체 개수"),
                                fieldWithPath("pageInfo.first").description("질문 답변 첫 페이지 여부"),
                                fieldWithPath("pageInfo.last").description("질문 답변 마지막 페이지 여부"),
                                fieldWithPath("pageInfo.hasNext").description("질문 답변 다음 페이지 여부"),
                                fieldWithPath("pageInfo.hasPrevious").description("질문 답변 이전 페이지 여부"),
                                fieldWithPath("data[].reply").description("질문 답변의 댓글 정보"),
                                fieldWithPath("data[].reply.replies[]").description("질문 답변의 댓글 목록"),
                                fieldWithPath("data[].reply.replies[].replyId").description("질문 답변의 댓글 ID"),
                                fieldWithPath("data[].reply.replies[].content").description("질문 답변의 댓글 내용"),
                                fieldWithPath("data[].reply.replies[].member").description("질문 답변의 댓글 작성자 정보"),
                                fieldWithPath("data[].reply.replies[].member.memberId").description("질문 답변의 댓글 작성자 ID"),
                                fieldWithPath("data[].reply.replies[].member.nickname").description("질문 답변의 댓글 작성자 닉네임"),
                                fieldWithPath("data[].reply.replies[].member.imageUrl").description("질문 답변의 댓글 작성자 이미지 URL"),
                                fieldWithPath("data[].reply.replies[].createdDate").description("질문 답변의 댓글 생성일"),
                                fieldWithPath("data[].reply.replies[].updatedDate").description("질문 답변의 댓글 수정일"),
                                fieldWithPath("data[].reply.pageInfo").description("질문 답변의 댓글 페이징 정보"),
                                fieldWithPath("data[].reply.pageInfo.page").description("질문 답변의 댓글 현재 페이지"),
                                fieldWithPath("data[].reply.pageInfo.size").description("질문 답변의 댓글 페이지 사이즈"),
                                fieldWithPath("data[].reply.pageInfo.totalPage").description("질문 답변의 댓글 전체 페이지 수"),
                                fieldWithPath("data[].reply.pageInfo.totalSize").description("질문 답변의 댓글 전체 개수"),
                                fieldWithPath("data[].reply.pageInfo.first").description("질문 답변의 댓글 첫 페이지 여부"),
                                fieldWithPath("data[].reply.pageInfo.last").description("질문 답변의 댓글 마지막 페이지 여부"),
                                fieldWithPath("data[].reply.pageInfo.hasNext").description("질문 답변의 댓글 다음 페이지 여부"),
                                fieldWithPath("data[].reply.pageInfo.hasPrevious").description("질문 답변의 댓글 이전 페이지 여부"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                )
        );
    }
    
    @Test
    @DisplayName("답변 단건 조회 API")
    void getAnswer() throws Exception {
        //given
        Long answerId = 1L;

        AnswerResponse answer = AnswerResponse.builder()
                .answerId(answerId)
                .content("content")
                .member(MemberInfo.of(createMember(1L)))
                .recommend(10)
                .recommendType(TypeEnum.UPVOTE)
                .reply(createReplyResponse(1))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        String result = objectMapper.writeValueAsString(ApiSingleResponse.ok(answer));

        given(answerService.findAnswer(anyLong())).willReturn(answer);

        //when
        ResultActions actions = mockMvc.perform(
                get("/answers/{answer-id}", answerId)
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
                                parameterWithName("answer-id").description("답변 ID")
                        ),
                        responseFields(
                                fieldWithPath("data").description("답변 정보"),
                                fieldWithPath("data.answerId").description("답변 ID"),
                                fieldWithPath("data.content").description("답변 내용"),
                                fieldWithPath("data.member").description("답변 작성자 정보"),
                                fieldWithPath("data.member.memberId").description("답변 작성자 ID"),
                                fieldWithPath("data.member.nickname").description("답변 작성자 닉네임"),
                                fieldWithPath("data.member.imageUrl").description("답변 작성자 이미지 URL"),
                                fieldWithPath("data.recommend").description("답변 추천수"),
                                fieldWithPath("data.recommendType").description(generateLinkCode(EnumType.class)),
                                fieldWithPath("data.createdDate").description("답변 생성일"),
                                fieldWithPath("data.updatedDate").description("답변 수정일"),
                                fieldWithPath("data.reply").description("질문 답변의 댓글 정보"),
                                fieldWithPath("data.reply.replies[]").description("질문 답변의 댓글 목록"),
                                fieldWithPath("data.reply.replies[].replyId").description("질문 답변의 댓글 ID"),
                                fieldWithPath("data.reply.replies[].content").description("질문 답변의 댓글 내용"),
                                fieldWithPath("data.reply.replies[].member").description("질문 답변의 댓글 작성자 정보"),
                                fieldWithPath("data.reply.replies[].member.memberId").description("질문 답변의 댓글 작성자 ID"),
                                fieldWithPath("data.reply.replies[].member.nickname").description("질문 답변의 댓글 작성자 닉네임"),
                                fieldWithPath("data.reply.replies[].member.imageUrl").description("질문 답변의 댓글 작성자 이미지 URL"),
                                fieldWithPath("data.reply.replies[].createdDate").description("질문 답변의 댓글 생성일"),
                                fieldWithPath("data.reply.replies[].updatedDate").description("질문 답변의 댓글 수정일"),
                                fieldWithPath("data.reply.pageInfo").description("질문 답변의 댓글 페이징 정보"),
                                fieldWithPath("data.reply.pageInfo.page").description("질문 답변의 댓글 현재 페이지"),
                                fieldWithPath("data.reply.pageInfo.size").description("질문 답변의 댓글 페이지 사이즈"),
                                fieldWithPath("data.reply.pageInfo.totalPage").description("질문 답변의 댓글 전체 페이지 수"),
                                fieldWithPath("data.reply.pageInfo.totalSize").description("질문 답변의 댓글 전체 개수"),
                                fieldWithPath("data.reply.pageInfo.first").description("질문 답변의 댓글 첫 페이지 여부"),
                                fieldWithPath("data.reply.pageInfo.last").description("질문 답변의 댓글 마지막 페이지 여부"),
                                fieldWithPath("data.reply.pageInfo.hasNext").description("질문 답변의 댓글 다음 페이지 여부"),
                                fieldWithPath("data.reply.pageInfo.hasPrevious").description("질문 답변의 댓글 이전 페이지 여부"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                )
        );
        
    }

    @Test
    @DisplayName("답변 수정 API")
    void updateAnswer() throws Exception {
        //given
        Long answerId = 1L;

        setDefaultAuthentication(1L);

        AnswerUpdateApiRequest request = AnswerUpdateApiRequest.builder()
                .content("content")
                .build();

        String content = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/answers/{answer-id}", answerId)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        setConstraintClass(AnswerUpdateApiRequest.class);

        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("답변 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        ),
                        requestFields(
                                fieldWithPath("content").description("답변 내용").attributes(getConstraint("content"))
                        )
                )
        );
    }

    @Test
    @DisplayName("답변 추천 API")
    void upvoteAnswer() throws Exception {
        //given
        Long answerId = 1L;

        setDefaultAuthentication(1L);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/answers/{answer-id}/upvote", answerId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("추천할 답변 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        )
                )
        );
    }

    @Test
    @DisplayName("답변 비추천 API")
    void downvoteAnswer() throws Exception {
        //given
        Long answerId = 1L;

        setDefaultAuthentication(1L);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/answers/{answer-id}/downvote", answerId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("비추천할 답변 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        )
                )
        );
    }

    @Test
    @DisplayName("답변 삭제 API")
    void deleteAnswer() throws Exception {
        //given
        Long answerId = 1L;
        setDefaultAuthentication(1L);

        //when
        ResultActions actions = mockMvc.perform(
                delete("/answers/{answer-id}", answerId)
                        .header("Authorization", "Bearer abc.def.ghi")
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isNoContent());

        //restDocs
        actions.andDo(
                documentHandler.document(
                        pathParameters(
                                parameterWithName("answer-id").description("삭제할 답변 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("accessToken")
                        )
                )
        );
    }

    @Test
    void createReply() {
    }
}